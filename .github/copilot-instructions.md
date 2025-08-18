## 프로젝트 개요
이 프로젝트는 채팅 기능을 제공하는 웹 애플리케이션입니다.
Java와 Spring Boot로 구축되었으며, 데이터 저장소로는 MySQL, Redis를 사용합니다.

## 폴더 구조
-  com/rabbitmqprac/application: 애플리케이션 레이어
- com/rabbitmqprac/config: 설정 관련 클래스
- com/rabbitmqprac/domain: 도메인 모델 클래스
  - /context: 도메인의 비즈니스 레이어
  - /persistence: 도메인의 영속성 레이어
- com/rabbitmqprac/global: 전역으로 관리되는 클래스
- com/rabbitmqprac/infra: security, stomp, batch, logging 등 외부 인프라 관련 클래스

## 라이브러리 및 프레임워크
- 백엔드: Java, Spring Boot, JPA, Spring Securit & JWT, Spring Batch
- DB: MySQL, Redis
- ETC: Docker, docker-compose, STOMP, WebSocket, RabbitMQ

## 코딩 규칙
코드 스타일은 config/checkstyle/rules.xml 파일에 정의된 규칙을 따릅니다.

## 코드 리뷰 작성법
- 한글로 작성할 것.
- 코드 제안은 config/checkstyle/rules.xml에 정의된 Lint 규칙을 따를 것.
