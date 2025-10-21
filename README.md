# spring-request-flow-practice
스프링 웹 애플리케이션의 요청 흐름을 이해하고, 다양한 데이터 접근 방법을 실습하며, 동시성 이슈까지 다뤄보는 학습용 프로젝트
-----------------------------
# Spring Web Flow & Data Access Practice

**스프링 웹 애플리케이션의 요청 흐름을 이해하고, 다양한 데이터 접근 방법을 실습하며, 동시성 이슈까지 다뤄보는 학습용 프로젝트입니다.**

---

## 프로젝트 구성

### 1. 요청 흐름 실습 (Controller → Service → Repository)
- 각 계층의 역할 분리 및 동작 확인
- 간단한 API를 통해 흐름 학습

### 2. 데이터 접근 기술 비교
- `JdbcTemplate` 기반 구현
- `JPA` EntityManager 기반 구현
- `Spring Data JPA` Repository 기반 구현
- 동일한 기능을 각각 다른 방식으로 구현하여 비교 학습

### 3. 추가 기능: 한정 수량 쿠폰 발급
- 선착순으로 발급되는 쿠폰 API
- 동시성 테스트 (멀티스레드 환경)
- Optimistic Lock, Synchronized, Redis 등을 활용한 처리 방식 실험 (옵션)

---

## 기술 스택

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA / JPA / JdbcTemplate
- H2 / MySQL
- JUnit 5

---

## 향후 학습 확장 아이디어

- 동시성 테스트 자동화 (JMeter, Gatling 등)
- Redis 기반 분산 락 실험
- 트랜잭션 전파/격리 수준 실습
- Kafka를 이용한 비동기 발급 처리 (확장)

---

## 만든 이유

처음 스프링을 학습하면서 단순히 동작하는 것보다는 **각 계층이 어떤 역할을 하는지**, **데이터 접근 방식이 어떻게 다른지**, **실제 서비스에서 자주 나오는 동시성 이슈를 어떻게 해결할 수 있는지**를 체험하기 위해 만든 실습용 프로젝트입니다.

