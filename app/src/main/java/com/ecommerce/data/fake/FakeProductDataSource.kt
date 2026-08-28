package com.ecommerce.data.fake

import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.domain.entity.ProductVariant

object FakeProductDataSource {
    val dummyProduct = listOf(
        Product(
            id = 1L,
            name = "Minyak Kayu Putih Asli Ambon",
            description = "Minyak kayu putih murni hasil penyulingan tradisional daun cajuput pilihan di Pulau Buru, Ambon. Memiliki aroma hangat menenangkan yang khas dan khasiat alami.",
            category = ProductCategory.PURE_OIL,
            benefits = "Membantu meredakan masuk angin, perut kembung, pegal-pegal, dan melindungi dari gigitan serangga.",
            composition = "100% Oleum Cajuputi Murni (Kadar Cineol > 60%)",
            usage = "Oleskan atau pijatkan secara merata pada bagian dada, perut, leher, atau bagian tubuh yang terasa nyeri.",
            imageUrl = "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?q=80&w=800&auto=format&fit=crop",
            variants = listOf(
                ProductVariant(id = 101L, productId = 1L, size = "30 ml", price = 35000L, stock = 25),
                ProductVariant(id = 102L, productId = 1L, size = "60 ml", price = 65000L, stock = 15),
                ProductVariant(id = 103L, productId = 1L, size = "100 ml", price = 100000L, stock = 8)
            )
        ),
        Product(
            id = 2L,
            name = "Minyak Nilam Murni (Patchouli Essential Oil)",
            description = "Essential oil minyak nilam kualitas ekspor dengan aroma earthy dan woody yang kuat. Cocok sebagai aromaterapi dan bahan dasar fiksasi parfum alami.",
            category = ProductCategory.PURE_OIL,
            benefits = "Meredakan stres, membantu relaksasi, merawat kesehatan kulit, serta pengusir serangga alami.",
            composition = "100% Pogostemon Cablin Oil (Pure Steam Distilled)",
            usage = "Teteskan 3-5 tetes pada diffuser ruangan, atau campurkan dengan carrier oil untuk pijat.",
            imageUrl = "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?q=80&w=800&auto=format&fit=crop",
            variants = listOf(
                ProductVariant(id = 201L, productId = 2L, size = "10 ml", price = 85000L, stock = 12),
                ProductVariant(id = 202L, productId = 2L, size = "30 ml", price = 220000L, stock = 5)
            )
        ),
        Product(
            id = 3L,
            name = "Minyak Cengkeh Alami (Clove Oil)",
            description = "Minyak daun cengkeh murni hasil distilasi uap. Kandungan eugenol tinggi memberikan sensasi hangat intens dan aroma rempah kuat.",
            category = ProductCategory.PURE_OIL,
            benefits = "Meringankan sakit gigi sementara, meredakan nyeri otot dan persendian, serta antiseptik alami.",
            composition = "100% Eugenia Caryophyllata Oil (Eugenol > 80%)",
            usage = "Untuk luar tubuh: oleskan pada area nyeri setelah dilarutkan dengan carrier oil.",
            imageUrl = "https://images.unsplash.com/photo-1547887537-6158d64c35b3?q=80&w=800&auto=format&fit=crop",
            variants = listOf(
                ProductVariant(id = 301L, productId = 3L, size = "15 ml", price = 45000L, stock = 20),
                ProductVariant(id = 302L, productId = 3L, size = "50 ml", price = 110000L, stock = 10)
            )
        ),
        Product(
            id = 4L,
            name = "Kayu Manis Batang Kering Pilihan (Ceylon Cinnamon)",
            description = "Kulit batang kayu manis kering pilihan dengan aroma manis aromatik dan rasa yang lembut alami. Dipanen dan dikeringkan secara higienis.",
            category = ProductCategory.DRIED_SPICE,
            benefits = "Menjaga kadar gula darah, kaya antioksidan alami, dan penghangat tubuh.",
            composition = "100% Cinnamomum Verum Bark Kering",
            usage = "Seduh 1 batang dengan air panas atau rebus bersama teh/kopi herbal favorit Anda.",
            imageUrl = "https://images.unsplash.com/photo-1509358271058-acd22cc93898?q=80&w=800&auto=format&fit=crop",
            variants = listOf(
                ProductVariant(id = 401L, productId = 4L, size = "100 gram", price = 28000L, stock = 30),
                ProductVariant(id = 402L, productId = 4L, size = "250 gram", price = 60000L, stock = 18)
            )
        ),
        Product(
            id = 5L,
            name = "Ekstrak Jahe Merah Bubuk Premium",
            description = "Ekstrak murni jahe merah tanpa pemanis buatan dan tanpa bahan pengawet. Rasa pedas hangat alami yang mantap.",
            category = ProductCategory.HERBAL_EXTRACT,
            benefits = "Meningkatkan daya tahan tubuh, menghangatkan tenggorokan, dan melancarkan sirkulasi darah.",
            composition = "100% Zingiber Officinale Var. Rubrum Extract",
            usage = "Seduh 1 sendok teh bubuk ke dalam 200ml air hangat, tambahkan madu sesuai selera.",
            imageUrl = "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?q=80&w=800&auto=format&fit=crop",
            variants = listOf(
                ProductVariant(id = 501L, productId = 5L, size = "100 gram", price = 40000L, stock = 22),
                ProductVariant(id = 502L, productId = 5L, size = "250 gram", price = 85000L, stock = 14)
            )
        )
    )
}