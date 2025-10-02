package io.github.wisely.web.http;


import io.github.wisely.core.exception.ThirdPartyException;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * WebClient过滤器，进行统一的异常处理
 *
 * @author Big_程
 * @version 2.0.0
 * @since 2025-09-03 14:51:00
 */
@Slf4j
public class WebClientFilter implements ExchangeFilterFunction {

    @Nonnull
    @Override
    public Mono<ClientResponse> filter(@Nonnull ClientRequest request, @Nonnull ExchangeFunction next) {
        return next.exchange(request)
                .flatMap(resp -> {
                    if (resp.statusCode().isError()) {
                        return resp
                                .bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("{} {} request error: \r\n{}", request.method(), request.url().getPath(), body);
                                    return Mono.error(ThirdPartyException.of(body));
                                });
                    }
                    return Mono.just(resp);
                });
    }
}
