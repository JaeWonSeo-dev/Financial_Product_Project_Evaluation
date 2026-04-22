# 금융상품 및 프로젝트 평가 시스템

금융상품과 투자 프로젝트를 **수익성 + 위험도** 관점에서 함께 평가하는 Spring Boot 포트폴리오 프로젝트입니다.

## 핵심 기능
- 대시보드: 등록 상품/프로젝트 수, 평균 수익률, 평균 위험도, 최근 평가 결과, 차트
- 금융상품 관리: 채권/대출/펀드/파생상품 CRUD
- 프로젝트 평가: 현금흐름 입력, NPV / IRR / Payback Period 계산
- 위험 분석: 낙관/기준/비관 시나리오, 할인율 민감도 분석, VaR 스타일 손실 추정
- REST API: `/api/dashboard`, `/api/products`, `/api/projects`, `/api/projects/{id}/evaluation`
- H2 기반 샘플 데이터 자동 로딩
- 계산 로직 테스트 포함

## 기술 스택
- Java 17
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Data JPA
- H2 Database
- Chart.js
- Gradle Wrapper

## 설계 의도
단순한 CRUD가 아니라, 금융상품 속성(기대수익률/할인율/위험등급/변동성)과 프로젝트 투자 타당성(NPV/IRR/Payback)을 함께 보여줘 **금융 도메인 이해**를 드러내는 포트폴리오로 구성했습니다.

## 도메인 구조
- `FinancialProduct`: 금융상품 마스터
- `EvaluationProject`: 프로젝트 기본 정보
- `ProjectCashFlow`: 프로젝트 연차별 현금흐름
- `FinancialCalculationEngine`: 계산 전용 엔진
- `DashboardService`: 요약 지표/차트 데이터 조합
- `ProjectEvaluationService`: 프로젝트 CRUD + 평가 응답 생성

## 계산 공식과 가정
### 1. NPV
`NPV = -InitialInvestment + Σ(CFt / (1+r)^t)`
- `r`은 할인율(%)
- `CFt`는 t년차 순현금흐름(유입 - 유출)

### 2. IRR
- Newton-Raphson 방식으로 직접 구현
- NPV가 0이 되는 할인율을 반복 계산
- 결과는 % 단위로 반환

### 3. Payback Period
- 누적 순현금흐름이 초기투자금을 회수하는 시점을 계산
- 중간연도 회수는 비율로 보간하여 소수점 연차로 반환

### 4. 시나리오 분석
- 낙관: 현금흐름 +15%, 할인율 -1%p
- 기준: 입력값 그대로
- 비관: 현금흐름 -15%, 할인율 +1.5%p

### 5. 민감도 분석
- 할인율을 기준값에서 -2%p ~ +2%p 변화시켜 NPV 변동 관찰

### 6. 위험 점수 / VaR 스타일 지표
- 위험 점수: 할인율 대비 IRR 스프레드, 회수기간, 현금흐름 변동성을 합성한 단순 포트폴리오용 점수
- VaR 스타일 손실: `투자금액 × 변동성 × 1.65(95% z-score)`

## 실행 방법
```bash
# Windows
.\gradlew.bat bootRun
```
실행 후 접속:
- 메인 화면: <http://localhost:8082>
- H2 Console: <http://localhost:8082/h2-console>
  - JDBC URL: `jdbc:h2:mem:finance`
  - username: `sa`
  - password: (빈값)

## 주요 화면
1. **대시보드**
   - 상품 수익률/위험도 차트
   - 최근 프로젝트 평가 결과 요약
2. **금융상품 관리**
   - 상품 등록/수정/삭제/상세 조회
3. **프로젝트 평가**
   - 현금흐름 입력
   - NPV/IRR/Payback/위험등급/시나리오/민감도 차트 확인

## REST API 예시
- `GET /api/dashboard`
- `GET /api/products`
- `POST /api/products`
- `GET /api/projects`
- `POST /api/projects`
- `GET /api/projects/{id}/evaluation`

## 테스트
```bash
.\gradlew.bat test
```
포함 테스트:
- NPV 계산
- IRR 계산
- Payback Period 계산
- 시나리오/민감도 분석 개수
- 평가 등급 판정

## 향후 개선 아이디어
- Monte Carlo 시뮬레이션 기반 위험 분석
- 사용자별 권한 관리
- PostgreSQL 전환 및 이력 테이블 분리
- PDF 평가 리포트 출력
