# 🏥 병원 환자 관리 시스템 (Hospital Management System)

Spring Boot 기반 **병원 환자 관리 시스템** 프로젝트입니다.
환자 및 방문 데이터를 **REST API**로 관리하며, **QueryDSL** 기반 동적 검색과 **Spring REST Docs + Asciidoctor**로 문서화됩니다.

> **Tech stack**: Spring Boot 3.5.4 | Java 17 | Gradle | Spring Data JPA | H2 (in-memory) | QueryDSL | Lombok | Validation | Spring REST Docs

---

## ✨ 핵심 기능

| 기능 | 설명 |
|------|------|
| 🏥 병원 | 등록 / 조회 / 수정 / 삭제 |
| 👤 환자 | 등록 / 조회 / 수정 / 삭제(Soft Delete)<br>동적 검색(QueryDSL)<br>환자등록번호: `병원ID(3자리)-병원별순번` (예: `003-12`)<br>동시성: PESSIMISTIC_WRITE + seq max 조회 후 +1 |
| 📅 방문 | 환자별 방문 등록 / 조회 / 수정 / 삭제 |
| 📄 문서화 | 테스트 스니펫 기반 REST Docs → Asciidoctor HTML |

---

## 🧱 도메인 모델

**Hospital**: `id`, `name`, `providerNumber`, `doctorName`

**Patient**: `id`, `hospital`, `seq`, `patientNumber`, `name`, `gender`, `birthDate`, `phone`, `address`, `status`<br>Soft Delete: `status='A'|'D'`<br>유니크 제약: `(hospital_id, seq)`

**Visit**: `id`, `hospital`, `patient`, `visitDate`, `visitStatus`, `visitType`, `visitCategory`

---

## 📂 패키지 구조
```
src/
 ├─ main/
 │   ├─ java/com/minlab/hospital
 │   │   ├─ application/service
 │   │   │   ├─ HospitalService.java
 │   │   │   ├─ PatientService.java
 │   │   │   └─ VisitService.java
 │   │   ├─ config
 │   │   │   ├─ DataInitializer.java
 │   │   │   └─ QuerydslConfig.java
 │   │   ├─ domain
 │   │   │   ├─ entity (Hospital, Patient, Visit)
 │   │   │   ├─ repository
 │   │   │   │   ├─ HospitalRepository.java
 │   │   │   │   ├─ PatientRepository.java (+ Custom/Impl, PredicateBuilder)
 │   │   │   │   └─ VisitRepository.java
 │   │   └─ presentation
 │   │       ├─ GlobalExceptionHandler.java
 │   │       ├─ controller (Hospital, Patient, Visit)
 │   │       └─ dto
 │   │           ├─ request (HospitalRequestDto, PatientRequestDto, PatientSearchRequestDto, VisitRequestDto)
 │   │           └─ response (HospitalResponseDto, PatientResponseDto, PatientSearchResponseDto, VisitResponseDto)
 │   └─ resources
 │       ├─ application.yml
 │       └─ static / templates
 └─ test/java/com/minlab/hospital/presentation (REST Docs 스니펫 생성)
```
---
## 🚀 빌드 & 실행
```bash
git clone https://github.com/min-lab101/hospital-api.git
cd hospital-api

# 개발용
./gradlew clean bootRun

# 배포용 Jar
./gradlew clean bootJar
java -jar build/libs/hospital-0.0.1-SNAPSHOT.jar
```

- 기본 포트: `http://localhost:8080`
- H2 콘솔: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:hospitaldb`, User: `sa`, Password: *(빈 값)*

---

## 📜 API 요약

| 리소스 | 경로 |
|--------|------|
| 🏥 병원 | `/api/hospitals` |
| 👤 환자 | `/api/hospitals/{hospitalId}/patients` |
| 📅 방문 | `/api/patients/{patientId}/visits` |

- Content-Type: `application/json`
> API 문서는 [여기](https://min-lab101.github.io/hospital-api/)에서 확인 가능합니다.

---

## 📘 REST Docs

```bash
./gradlew test asciidoctor
open build/asciidoc/html5/index.html
```

- bootJar 시 정적 문서(`static/docs`) 포함
- 문서 소스: `src/docs/asciidoc/index.adoc`

---

## 🧰 개발 팁

- **환자등록번호 생성**:
```java
PatientNumberGenerator.generate(hospitalId, nextSeq)
// String.format("%03d-%d", hospitalId, nextSeq)
```
- Soft Delete 필터링: Repository 조회 시 `status='A'`
- QueryDSL 동적 조건: `PatientPredicateBuilder` 사용 (name, patientNumber, birthDate)
- 초기 데이터: `DataInitializer` → 기본 병원 2건 생성

---

## 🔗 상세 자료
상세 설계 노트는 [Notion 문서](https://expensive-pan-fed.notion.site/hospital-api-251221c235a18069b2b8e462fc069dc5?pvs=73)에서 확인 가능합니다.