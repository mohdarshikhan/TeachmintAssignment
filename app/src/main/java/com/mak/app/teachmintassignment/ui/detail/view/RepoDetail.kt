package com.mak.app.teachmintassignment.ui.detail.view

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.mak.app.teachmintassignment.R
import com.mak.app.teachmintassignment.domain.repo.model.Items

@Composable
fun RepoDetail(
    navController: NavHostController,
    item: Items
) {

    val imageUrl = item.owner?.avatarUrl
    val name = item.name
    val description = item.description
    val projectLink = item.htmlUrl

    val linkStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .size(84.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        if (name != null) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (projectLink != null) {
            val context = LocalContext.current
            Text(
//                text = projectLink,
                text = projectLink.linkify(linkStyle),
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable {

                    // Create an Intent with ACTION_VIEW and the URL
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(projectLink))

                    // Start the activity with the Intent
                    context.startActivity(intent)
                }
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun PreviewRepoDetail() {
    val navController = rememberNavController()
    RepoDetail(navController, Items())
}

fun String.linkify(
    linkStyle: SpanStyle,
) = buildAnnotatedString {
    append(this@linkify)

    val spannable = SpannableString(this@linkify)
    Linkify.addLinks(spannable, Linkify.WEB_URLS)

    val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
    for (span in spans) {
        val start = spannable.getSpanStart(span)
        val end = spannable.getSpanEnd(span)

        addStyle(
            start = start,
            end = end,
            style = linkStyle,
        )
        addStringAnnotation(
            tag = "URL",
            annotation = span.url,
            start = start,
            end = end
        )
    }
}

fun AnnotatedString.urlAt(position: Int, onFound: (String) -> Unit) =
    getStringAnnotations("URL", position, position).firstOrNull()?.item?.let {
        onFound(it)
    }