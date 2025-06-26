## 취미 기록 Web Page V6

취미 기록 Web Page V6
이 프로젝트는 개인의 취미를 기록하고 공유할 수 있는 웹사이트입니다. 
V6에서는 **JWT 기반 로그인 전환, 페이징 및 검색 쿼리 최적화, 댓글 기반 실시간 알림 개선**을 통해 사용자 경험을 더욱 향상했습니다.
사용자는 다양한 취미를 자유롭게 등록하고, 활동 내용을 정리하여 언제든지 돌아볼 수 있습니다. 
취미를 기록하는 즐거움을 통해 더 많은 사람들과 소통하고, 새로운 취미를 발견하는 기회를 가질 수 있습니다. 
나만의 취미 세상을 만들어보세요!

| **버전** | **링크**       |
|----------|----------------|
| 취미 기록 게시판 V1       | [취미 Web Page V1](https://github.com/kjyend/hobbyproject) |
| 취미 기록 게시판 V2       | [취미 Web Page V2](https://github.com/kjyend/hobbyprojectV2) |
| 취미 기록 게시판 V3       | [취미 Web Page V3](https://github.com/kjyend/hobbyprojectV3) |
| 취미 기록 게시판 V4       | [취미 Web Page V4](https://github.com/kjyend/hobbyprojectV4) |
| 취미 기록 게시판 V5       | [취미 Web Page V5](https://github.com/kjyend/hobbyprojectV5) |
| 취미 기록 게시판 V6       | [취미 Web Page V6](https://github.com/kjyend/hobbyprojectV6) |

## 개요

개발 기간 : 2025.3.12 ~ 2025.4.11

멤버 : 김준영(FE/BE)


## 시작 가이드

Installation
```
$ git clone https://github.com/kjyend/hobbyprojectV6.git
$ cd hobbyprojectV6
```
Backend
```
$ ./gradlew build
$ ./gradlew bootRun
```

## 기술 스택

Language (BE) : Java

Language (FE) : JavaScript

Library & Framework : Spring Boot, Spring, Thymeleaf, Hibernate 

Database : MySQL, Redis

ORM : JPA 

Messaging : RabbitMQ 

Environment : IntelliJ, Git, GitHub 

Monitoring : Prometheus, Grafana

Performance Testing : JMeter

## V6 주요 변경 사항
✅ JWT 기반 인증 구조 도입

* 기존의 세션 기반 로그인 방식에서 JWT(Json Web Token) 방식으로 변경

✅ 검색 기능 최적화

* 검색 시 LIKE '%keyword%'에서 LIKE 'keyword%'로 쿼리 패턴 변경
* 인덱스 활용이 가능해져 Full Table Scan 방지 및 성능 향상

✅ 페이징 최적화

* Google-style 페이징 방식을 참고하여 기본 페이지 조회 시 count 쿼리 생략
* 마지막 페이지 여부를 조회 결과로 판단해 전체 페이지 계산 없이 처리
* 유효하지 않은 페이지 요청 시에만 count 쿼리 실행 → 성능 최적화

✅ 실시간 알림 시스템 개선 (Redis → RabbitMQ)

* 좋아요 알림에서 발생하는 과도한 트래픽 문제 해결을 위해 댓글 작성 시에만 알림 전송
* Redis SSE 기반 알림 시스템을 RabbitMQ 기반 비동기 메시징 구조로 전환
* 메시지 브로커로 확장성과 안정성 확보, SSE를 통한 클라이언트 실시간 알림 유지


## 화면 구성

1. 첫 페이지 조회
![스크린샷_13-4-2025_203345_localhost](https://github.com/user-attachments/assets/3dd1052b-17da-495d-897a-948778e85d81)


2. 데이터가 존재하지 않은 페이지 조회
![스크린샷_13-4-2025_203442_localhost](https://github.com/user-attachments/assets/f69a9a2e-7a5b-4793-a4c8-f541086e54a6)


3. 데이터가 존재하는 마지막 페이지 조회
![스크린샷_13-4-2025_203512_localhost](https://github.com/user-attachments/assets/034e0e36-cb40-42bc-8c12-25a7dce4edfe)


4. 댓글 작성 시 알림 표시


https://github.com/user-attachments/assets/a9b3cc6b-1373-4597-a542-edda1596c69a



## ERD 
![스크린샷 2025-04-11 214057](https://github.com/user-attachments/assets/38bdb563-7049-4f01-a980-88a74f3d2869)

