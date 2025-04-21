import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object PaddingConstants {

    // Tekil Dp değerleri
    val ExtraSmall: Dp = 4.dp
    val Small: Dp = 8.dp
    val Medium: Dp = 16.dp
    val Large: Dp = 24.dp
    val ExtraLarge: Dp = 32.dp

    // Sayfa kenar boşluğu
    val PagePadding: Dp = Medium

    // Sık kullanılan PaddingValues tanımları
    val HorizontalMedium: PaddingValues = PaddingValues(horizontal = Medium)
    val VerticalMedium: PaddingValues = PaddingValues(vertical = Medium)
    val ScreenPadding: PaddingValues = PaddingValues(all = PagePadding)
}
