package com.alchemain.rx.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TransferEncodingAwareHttpRequestDecoderTest {
	@Test
	public void handlesChunkedTransferEncodingWithContentLength() {
		TransferEncodingAwareHttpRequestDecoder decoder =
				new TransferEncodingAwareHttpRequestDecoder();
		EmbeddedChannel channel = new EmbeddedChannel(decoder);

		channel.writeInbound(Unpooled.copiedBuffer(
				"POST /work HTTP/1.1\r\n"
						+ "Host: localhost\r\n"
						+ "Transfer-Encoding: chunked\r\n"
						+ "Content-Length: 5\r\n"
						+ "\r\n"
						+ "0\r\n\r\n",
				StandardCharsets.US_ASCII));

		HttpRequest request = channel.readInbound();
		assertEquals(1L, decoder.getConflictingFramingHeaderCount());
		assertFalse(request.headers().contains(HttpHeaderNames.CONTENT_LENGTH));

		channel.finishAndReleaseAll();
	}
}
