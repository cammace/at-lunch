package com.alltrails.atlunch.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alltrails.atlunch.R
import com.alltrails.atlunch.ui.theme.AtLunchTheme

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    Surface(
        color = Color(0xFFEFEFEC),
        contentColor = Color(0xFF656E5E),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                modifier = Modifier.size(16.dp),
                contentDescription = stringResource(R.string.search_title)
            )
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                onValueChange = {
                    if ("\n" !in it) onSearchQueryChanged(it)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchExplicitlyTriggered()
                    },
                ),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        onSearchFocusChange(it.isFocused)
                    }
                    .focusRequester(focusRequester)
                    .onKeyEvent {
                        if (it.key == Key.Enter) {
                            onSearchExplicitlyTriggered()
                            true
                        } else {
                            false
                        }
                    },
            )

            // Clear the search query if the user clicks the close button.
            Spacer(Modifier.width(8.dp))

            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star), // TODO replace icon with close.
                        modifier = Modifier.size(16.dp),
                        contentDescription = stringResource(id = R.string.search_cd_clear_search_text),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    AtLunchTheme {
        SearchBar(
            searchQuery = "",
            onSearchQueryChanged = {},
            onSearchTriggered = {},
            onSearchFocusChange = {}
        )
    }
}
