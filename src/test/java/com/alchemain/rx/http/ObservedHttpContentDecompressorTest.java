package com.alchemain.rx.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObservedHttpContentDecompressorTest {
	@Test
	public void createsAndUsesGzipContentDecoder() {
		ObservedHttpContentDecompressor decompressor = new ObservedHttpContentDecompressor();
		EmbeddedChannel pipeline = new EmbeddedChannel(decompressor);
		byte[] content = "reactor".getBytes(StandardCharsets.UTF_8);
		ByteBuf compressed = gzip(content);

		HttpResponse response =
				new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_ENCODING, HttpHeaderValues.GZIP);
		pipeline.writeInbound(response);
		pipeline.writeInbound(new DefaultHttpContent(compressed));
		pipeline.writeInbound(LastHttpContent.EMPTY_LAST_CONTENT);

		HttpResponse decodedResponse = pipeline.readInbound();
		assertEquals(1L, decompressor.getCreatedDecoderCount());
		assertTrue(!decodedResponse.headers().contains(HttpHeaderNames.CONTENT_ENCODING));

		pipeline.finishAndReleaseAll();
	}

	private ByteBuf gzip(byte[] content) {
		EmbeddedChannel encoder =
				new EmbeddedChannel(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
		encoder.writeOutbound(Unpooled.wrappedBuffer(content));
		encoder.finish();

		ByteBuf compressed = Unpooled.buffer();
		ByteBuf part;
		while ((part = encoder.readOutbound()) != null) {
			compressed.writeBytes(part);
			part.release();
		}
		return compressed;
	}
}
