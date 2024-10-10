package com.alltrails.atlunch.ui.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.atlunch.R
import com.alltrails.atlunch.ui.theme.AtLunchTheme


@Composable
fun ToggleListMapFab(
    modifier: Modifier = Modifier,
    isToggledToMap: Boolean = false,
    onFabToggled: (Boolean) -> Unit = {}
) {
    ExtendedFloatingActionButton(
        onClick = {
            onFabToggled(isToggledToMap)
        },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.small,
        icon = {
            Icon(
                painter = painterResource(id = if (isToggledToMap) R.drawable.ic_list else R.drawable.ic_map_filled),
                contentDescription = null // decorative element
            )
        },
        text = { Text(text = stringResource(if (isToggledToMap) R.string.list else R.string.map)) },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun ToggleListMapFabPreview() {
    AtLunchTheme {
        ToggleListMapFab()
    }
}
