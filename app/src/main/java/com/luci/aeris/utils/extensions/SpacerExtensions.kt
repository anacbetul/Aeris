import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height

// Extension functions for Spacer
fun Modifier.verticalSpacer(space: Dp) = this.then(Modifier.height(space))
fun Modifier.horizontalSpacer(space: Dp) = this.then(Modifier.width(space))
