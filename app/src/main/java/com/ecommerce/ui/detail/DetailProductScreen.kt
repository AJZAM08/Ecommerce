package com.ecommerce.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.ui.theme.PrimaryGreen
import com.ecommerce.ui.theme.SecondaryBrown
import com.ecommerce.utils.formatRupiah
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProdukScreen(
    viewModel: DetailProductViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (uiState is DetailProdukUiState.Success) {
                val state = uiState as DetailProdukUiState.Success
                val isOutOfStock = state.selectedVariant.stock <= 0

                Surface(
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Stepper Kuantitas (- / +)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.onQuantityDecrease() },
                                enabled = state.quantity > 1 && !isOutOfStock,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Kurang", modifier = Modifier.size(16.dp))
                            }

                            Text(
                                text = "${state.quantity}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            IconButton(
                                onClick = { viewModel.onQuantityIncrease() },
                                enabled = state.quantity < state.selectedVariant.stock && !isOutOfStock,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Tombol Tambah ke Keranjang
                        Button(
                            onClick = {
                                viewModel.addToCart {
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${state.product.name} (${state.selectedVariant.size}) berhasil ditambahkan!",
                                            actionLabel = "Lihat Keranjang",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onNavigateToCart()
                                        }
                                    }
                                }
                            },
                            enabled = !isOutOfStock,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isOutOfStock) "Stok Habis" else "Tambah ke Keranjang")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is DetailProdukUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            is DetailProdukUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadProductDetail() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
            is DetailProdukUiState.Success -> {
                val product = state.product

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Foto Produk
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f)
                            .background(Color(0xFFEFEFEF))
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        // Kategori
                        val categoryName = when (product.category) {
                            ProductCategory.PURE_OIL -> "Minyak Murni"
                            ProductCategory.DRIED_SPICE -> "Rempah Kering"
                            ProductCategory.HERBAL_EXTRACT -> "Ekstrak Herbal"
                        }
                        Text(
                            text = categoryName,
                            color = SecondaryBrown,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Nama Produk
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Harga Varian Terpilih
                        Text(
                            text = formatRupiah(state.selectedVariant.price),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Pilihan Varian Ukuran
                        Text("Pilih Ukuran / Varian:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            product.variants.forEach { variant ->
                                FilterChip(
                                    selected = variant.id == state.selectedVariant.id,
                                    onClick = { viewModel.onVariantSelected(variant) },
                                    label = { Text(variant.size) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        // Status Stok Varian
                        Spacer(modifier = Modifier.height(4.dp))
                        if (state.selectedVariant.stock <= 0) {
                            Text("Stok varian ini habis", color = Color.Red, fontSize = 12.sp)
                        } else {
                            Text("Tersedia: ${state.selectedVariant.stock} item", color = SecondaryBrown, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Deskripsi
                        Text("Deskripsi Produk", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(product.description, lineHeight = 20.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Manfaat
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("🌿 Manfaat & Khasiat", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(product.benefits, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Komposisi
                        Text("Komposisi Bahan:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(product.composition, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

                        Spacer(modifier = Modifier.height(12.dp))

                        // Cara Pakai
                        Text("Petunjuk Penggunaan:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(product.usage, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}