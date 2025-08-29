FROM alpine:latest

# 2025.08.30 기준 최신 LTS 버전
ENV PROMETHEUS_VERSION=3.5.0

# Prometheus 설치
RUN wget https://github.com/prometheus/prometheus/releases/download/v${PROMETHEUS_VERSION}/prometheus-${PROMETHEUS_VERSION}.linux-amd64.tar.gz \
    && tar xvf prometheus-${PROMETHEUS_VERSION}.linux-amd64.tar.gz \
    && mv prometheus-${PROMETHEUS_VERSION}.linux-amd64/prometheus /bin/prometheus \
    && mv prometheus-${PROMETHEUS_VERSION}.linux-amd64/promtool /bin/promtool \
    && rm -rf prometheus-${PROMETHEUS_VERSION}.linux-amd64*

# envsubst 설치
RUN apk add --no-cache gettext

# 설정 파일 복사
COPY config/prometheus-env.yml /etc/prometheus/prometheus-env.yml

ENTRYPOINT ["/bin/sh", "-c", "envsubst < /etc/prometheus/prometheus-env.yml > /etc/prometheus/prometheus.yml && exec prometheus --web.enable-lifecycle --enable-feature=expand-external-labels --config.file=/etc/prometheus/prometheus.yml"]
