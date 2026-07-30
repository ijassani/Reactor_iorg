package com.alchemain.rx.http;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.HttpContentDecompressor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * HTTP content decompressor that records supported content encodings for which
 * Netty creates a decoder.
 *
 * <p>Install this handler after an HTTP response decoder or
 * {@code HttpClientCodec} in a Netty client pipeline.</p>
 */
public class ObservedHttpContentDecompressor extends HttpContentDecompressor {
	private final AtomicLong createdDecoderCount = new AtomicLong();

	@Override
	protected EmbeddedChannel newContentDecoder(String contentEncoding) throws Exception {
		EmbeddedChannel decoder = super.newContentDecoder(contentEncoding);
		if (decoder != null) {
			createdDecoderCount.incrementAndGet();
		}
		return decoder;
	}

	public long getCreatedDecoderCount() {
		return createdDecoderCount.get();
	}
}
