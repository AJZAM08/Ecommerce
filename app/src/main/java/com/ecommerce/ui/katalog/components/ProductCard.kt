package com.ecommerce.ui.katalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.ui.theme.PrimaryGreen
import com.ecommerce.ui.theme.SecondaryBrown
import com.ecommerce.utils.formatRupiah

@Composable
fun ProductCard(
    product: Product,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val minPrice = product.variants.minOfOrNull { it.price } ?: 0L
    val totalStock = product.variants.sumOf { it.stock }

    Card(
        modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFFEAEAEA))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
            Column(
                Modifier.padding(10.dp)
            ) {
                val categoryName = when (product.category) {
                    ProductCategory.PURE_OIL -> "Minyak Murni"
                    ProductCategory.DRIED_SPICE -> "Rempah Kering"
                    ProductCategory.HERBAL_EXTRACT -> "Ekstrak Herbal"
                }

                Text(
                    categoryName,
                    fontSize = 11.sp,
                    color = SecondaryBrown,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    if (product.variants.size > 1) "Mulai ${formatRupiah(minPrice)}" else formatRupiah(minPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )

                Spacer(Modifier.height(4.dp))

                if (totalStock <= 0) {
                    Text(
                        "Stok Habis",
                        fontSize = 11.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                } else if (totalStock < 10) {
                    Text(
                        "Sisa $totalStock item",
                        fontSize = 11.sp,
                        color = SecondaryBrown,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}