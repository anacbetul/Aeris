import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
@Composable
fun AtomicOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    prefix:@Composable() (() -> Unit)
) {
    val isPasswordVisible = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            maxLines = 1,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPassword && !isPasswordVisible.value) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            leadingIcon = prefix,
            trailingIcon = {
                if (isPassword) {
                    IconButton(
                        onClick = {
                            isPasswordVisible.value = !isPasswordVisible.value
                        }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible.value)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
