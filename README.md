# Spring Web Flow & Data Access Practice

**스프링 웹 애플리케이션의 요청 흐름을 이해하고, JWT 기반 인증/인가를 적용 및  
다양한 데이터 접근 방법을 실습하며,  
객체지향 설계와 테스트 코드, 동시성 문제까지 다뤄보는 학습용 프로젝트입니다.**

---

# 프로젝트 소개

- Spring MVC 요청 흐름(Filter → DispatcherServlet → Controller → Service → Repository → DB) 실습  
- Filter, Controller, Service 계층에서 발생하는 예외 처리 실습  
- JWT 기반 인증/인가 적용  
- JdbcTemplate, JPA, Spring Data JPA 3가지 데이터 접근 기술 학습 및 비교  
- 쿠폰 발급 API를 통한 동시성 문제 실습 및 Jmeter로 테스트  
- 객체지향 설계 원칙(OOP) 적용 및 JUnit 테스트 코드 작성  

---

## 기술 스택

**언어 / 프레임워크**  
- Java 21, Spring Boot 3.5.6, Spring Web, Spring Security, Spring Data JPA, JdbcTemplate  

**데이터베이스**  
- H2  

**빌드 및 플러그인**  
- Gradle, Spring Dependency Management, Lombok  

**테스트 / 검증**  
- JUnit 5, Mockito, Spring Security Test  

**API / 테스트 도구**  
- Postman (API 테스트)  
- JMeter (부하 테스트)  

---

## 프로젝트 구조

```plaintext
src/
└─ main/java/hyos1/myapp/
   ├─ common/
   │  ├─ exception     # 예외 처리 관련 클래스
   │  └─ handler       # 공통 핸들러
   ├─ config           # 애플리케이션 설정 및 보안 관련 클래스
   ├─ controller       # REST API 컨트롤러
   ├─ dto/
   │  ├─ request       # 요청 DTO
   │  └─ response      # 응답 DTO
   ├─ entity           # JPA 엔티티 클래스
   ├─ enums            # 프로젝트 내 사용되는 Enum 정의
   ├─ filter           # JWT 인증 필터 등 요청 필터
   ├─ repository       # 데이터베이스 접근 레포지토리
   ├─ service          # 비즈니스 로직 처리 서비스
   └─ web              # 애플리케이션 진입점 및 웹 관련 클래스
```
---

## 프로젝트 구성

### 1. 요청 흐름 실습 (Filter -> DispatcherServlet -> Controller → Service → Repository)  
- 각 계층의 역할 분리 및 동작 확인  
- 간단한 API를 통해 흐름 학습  

### 2. JWT 인증/인가  
- 로그인 시 JWT 발급  
- Filter에서 JWT 검증 후 사용자 정보 추출  
- Controller/Service에서 인증된 사용자 정보 활용  

### 3. 예외 흐름(Exception Flow) 실습  
- Filter 단계에서 발생한 예외 → Filter 내부 처리  
- Controller/Service 단계에서 발생한 예외 → GlobalExceptionHandler 처리  

### 4. 데이터 접근 기술 비교  
- JdbcTemplate 기반 구현  
- JPA EntityManager 기반 구현  
- Spring Data JPA Repository 기반 구현  
- 동일한 기능을 각각 다른 방식으로 구현하여 비교 학습  

### 5. 조회 시 N + 1 문제 학습  
- 단일 엔티티: fetch join  
- 컬렉션 엔티티: @BatchSize  

### 6. 쿠폰 발급 + 동시성 제어 실습  
- 한정 수량 쿠폰 발급  
- JMeter를 사용하여 멀티스레드 환경 동시성 문제 테스트  
- Pessimistic Lock, 락 획득을 위해 대기시간 3초 설정  

### 7. 테스트 코드 작성  
- JUnit 기반 단위 테스트, 통합 테스트 작성  
- JWT 인증, 요청 흐름, 쿠폰 발급 동시성 검증  

---

## 만든 이유

스프링을 학습하면서 단순히 기능을 따라 만드는 것만으로는 ‘왜 이렇게 동작하는지’를 이해하기 어렵다고 느꼈습니다.  
Spring Data JPA의 편리한 기능을 사용하는 것조차 어려워서 문제가 발생하면 내부가 어떻게 동작하는지 모르기에 해결하는 것도 힘들었습니다.  

그래서 요청이 들어오고 응답이 나가기까지의 전체 흐름을 직접 확인하고,  
계층별 책임과 예외 전파 방식을 실습하며,  
JdbcTemplate, 순수 JPA, Spring Data JPA를 모두 적용해보면서 기술별 동작 차이와 문제 상황에서의 대응 방식을 이해하기 위해 이 프로젝트를 만들었습니다.  

복잡하게 만들기보다는 기본 역량을 다지는 것을 목표로 했습니다.
