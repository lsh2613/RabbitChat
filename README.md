### 01. 프로젝트 설명
- Spring + RabbitMQ + STOMP를 통한 실시간 채팅 서비스
- JWT를 통한 세션 관리
- DB 활용
  - | DB | 사용 이유          |
    |---|----------------|
    | H2 | 메인 DB          |
    | MongoDB | 채팅 내역 저장       |
    | Redis | 실시간 채팅방 참가자 관리 |

### 02. 핵심 기능
- 실시간 채팅
- 채팅방 메시지의 읽지 않은 사용자의 수를 실시간으로 반영
- UI를 통한 데이터 시각화 및 편의성 제공

### 03. 사용 기술
- `Spring Boot 3.1`, `Spring Data JPA`
- `Docker`, `docker-compose`
- `H2`, `MongoDB`, `Redis`
- `RabbitMQ`, `STOMP`
- `JWT`
- `HTML`, `CSS`, `JS`

### 04. 이슈
- [기술 스택 선정 이유](https://lsh2613.tistory.com/260#1.%20RabbitMQ%20%EC%84%A0%ED%83%9D%20%EC%9D%B4%EC%9C%A0-1)
- [STOMP-테스트 실패](https://lsh2613.tistory.com/260#3.%20%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EC%A4%91%20%EB%B3%80%EC%88%98%20%EB%B0%9C%EC%83%9D-1)
- [채팅 내역을 MongoDB에 저장하는 이유](https://lsh2613.tistory.com/261#1.%20%EC%B1%84%ED%8C%85%20%EB%82%B4%EC%97%AD%EC%9D%84%20MongoDB%EC%97%90%20%EC%A0%80%EC%9E%A5%ED%95%98%EB%8A%94%20%EC%9D%B4%EC%9C%A0-1)
- [읽음/안 읽음 기능 적용](https://lsh2613.tistory.com/262#1.%20%EC%9D%BD%EC%9D%8C%2F%EC%95%88%20%EC%9D%BD%EC%9D%8C-1)
- [JWT + Session 적용 이유](https://lsh2613.tistory.com/263#2.%20JWT%2C%20Session%20%EB%8C%80%EC%8B%A0%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%20%EA%B1%B4%EB%8D%B0%20%EC%99%9C%20%EA%B5%B3%EC%9D%B4%20%EB%91%98%20%EB%8B%A4%20%EC%82%AC%EC%9A%A9%ED%95%A0%EA%B9%8C%3F-1)

### 05. 시작하기
**1. 프로젝트 불러오기**
``` bash
git clone https://github.com/lsh2613/RabbitMQ.git <원하는 경로>
cd <원하는 경로>
```

**2. docker-compose 실행**

``` bash
docker-compose up -d
```

**3. 애플리케이션 실행**
``` bash
./gradlew bootRun
```

**4. 채팅 테스트**
1. `localhost:8080`를 통해 API UI에 접속
2. 회원 가입, 채팅방 생성, 채팅방 참가 등 필요한 api 호출
3. 두 개 이상의 브라우저를 열고, 각 브라우저에서 채팅방에 참가
4. 각 브라우저에서 채팅 메시지를 전송하고, 실시간으로 메시지가 전달되는지 확인
