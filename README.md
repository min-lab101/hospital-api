# 병원 환자 관리 시스템 (Hospital Management System)

Spring Boot 기반의 **병원 환자 관리 시스템** 프로젝트입니다.  
병원의 환자 데이터와 방문(내원) 기록을 **REST API**로 관리하며, **QueryDSL**을 통한 동적 검색과 **Spring REST Docs + Asciidoctor**로 API 문서화를 제공합니다.

> **Tech stack**: Spring Boot 3.5.4, Java 17, Gradle, Spring Data JPA, H2 DB (in-memory), QueryDSL, Lombok, Validation, Spring REST Docs
---
## ✨ 핵심 기능

- **병원(Hospital)**: 등록, 조회(상세/목록), 수정, 삭제
- **환자(Patient)**: 등록, 조회(상세/목록), 수정, 삭제(**Soft Delete**), 동적 검색(QueryDSL)
- **방문(Visit)**: 환자별 방문 등록, 조회(상세/목록), 수정, 삭제
- **환자등록번호 생성 규칙**: `병원ID(3자리)-병원별순번` (예: `003-12`)
    - 동시성 제어를 위해 `PESSIMISTIC_WRITE` 락으로 병원별 `seq` 최대값 조회 후 +1 (Repository: `findMaxSeqByHospitalForUpdate`)
- **문서화**: 테스트 스니펫 기반 REST Docs → Asciidoctor HTML 빌드
---
## 🧱 도메인 모델

### Hospital
- `id`, `name`, `providerNumber (요양기관번호)`, `doctorName`

### Patient
- `id`, `hospital (연관)`, `seq (병원별 순번)`, `patientNumber`, `name`, `gender`, `birthDate`, `phone`, `address`, `status`
- **Soft Delete**: `status` = `'A' | 'D'` (`softDelete()` 호출 시 `'D'`)
- 유니크 제약: `(hospital_id, seq)` 유니크

### Visit
- `id`, `hospital (연관)`, `patient (연관)`, `visitDate`, `visitStatus`, `visitType`, `visitCategory`
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

### 사전 요구
- **Java 17** (Gradle Wrapper가 자동 사용)
- 로컬에서 추가 DB 설치 불필요 (H2 in-memory)

### 실행
```bash
# 프로젝트 내려받기
git clone https://github.com/min-lab101/hospital-api.git
cd hospital-api

./gradlew clean bootRun
# 또는 배포용 Jar
./gradlew clean bootJar
java -jar build/libs/hospital-0.0.1-SNAPSHOT.jar
```

- 기본 포트: `http://localhost:8080`
- H2 콘솔: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:hospitaldb`, User: `sa`, Password: *(빈 값)*

> `src/main/resources/application.yml` 에서 H2 및 JPA 설정을 확인할 수 있습니다.

---
## 📜 API 요약

### Base Paths
- **병원**: `/api/hospitals`
- **환자**: `/api/hospitals/{hospitalId}/patients`
- **방문**: `/api/patients/{patientId}/visits`

### 표기 규칙
- 요청/응답은 `application/json`
> API 문서는 [여기](https://min-lab101.github.io/hospital-api/)에서 확인 가능합니다.
---
## 📘 REST Docs (Asciidoctor)

테스트 실행 시 생성되는 스니펫을 Asciidoctor로 HTML 문서화합니다.

```bash
./gradlew test asciidoctor
# 결과 보기 (macOS 예시)
open build/asciidoc/html5/index.html
```

`bootJar` 시 산출물에 정적 문서(`static/docs`)가 포함되도록 설정되어 있습니다.

문서 소스: `src/docs/asciidoc/index.adoc`
---
## 🧰 개발 팁

- **환자등록번호 생성**: `PatientNumberGenerator.generate(hospitalId, nextSeq)` → `String.format("%03d-%d", hospitalId, nextSeq)`
- **Soft Delete 필터링**: Repository 조회 시 `status='A'` 조건 사용
- **QueryDSL 동적 조건**: `PatientPredicateBuilder` 로 `name`(contains), `patientNumber`(eq), `birthDate`(eq) 조합
- **초기 데이터**: 애플리케이션 시작 시 `DataInitializer` 가 기본 병원 2건을 입력
---
## 상세 자료
상세 설계 노트는 [Notion 문서](https://expensive-pan-fed.notion.site/hospital-api-251221c235a18069b2b8e462fc069dc5?pvs=73)에서 확인 가능합니다.