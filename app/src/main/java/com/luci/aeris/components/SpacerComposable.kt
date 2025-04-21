import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalSpacer4() = Spacer(modifier = Modifier.height(SpacerConstants.ExtraSmall))

@Composable
fun VerticalSpacer8() = Spacer(modifier = Modifier.height(SpacerConstants.Small))

@Composable
fun VerticalSpacer16() = Spacer(modifier = Modifier.height(SpacerConstants.Medium))

@Composable
fun VerticalSpacer24() = Spacer(modifier = Modifier.height(SpacerConstants.Large))

@Composable
fun HorizontalSpacer4() = Spacer(modifier = Modifier.width(SpacerConstants.ExtraSmall))

@Composable
fun HorizontalSpacer8() = Spacer(modifier = Modifier.width(SpacerConstants.Small))

@Composable
fun HorizontalSpacer16() = Spacer(modifier = Modifier.width(SpacerConstants.Medium))

@Composable
fun HorizontalSpacer24() = Spacer(modifier = Modifier.width(SpacerConstants.Large))
