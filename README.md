|    기능    | HTTP Method |                 URL                  | request  | response |       상태 코드        |
|:--------:|:-----------:|:------------------------------------:|:--------:|:--------:|:------------------:|
|  일정 등록   |   `POST`    |         /task/schedules/add          | 요청 body  |  등록 정보   |     200: 정상등록      |
|  일정 조회   |    `GET`    |    /task/schedules/{schedule_id}     | 요청 param | 단건 응답 정보 |     200: 정상조회      |
| 일정 모두 조회 |    `GET`    |           /task/schedules/           | 요청 param | 다건 응답 정보 |     200: 정상조회      |
|  일정 수정   |   `POST`    |  /task/schedules/{schedule_id}/edit  | 요청 body  |  수정 정보   |     200: 정상수정      |
|  일정 삭제   |  `DELETE`   | /task/schedules/{schedule_id}/delete | 요청 param |    -     |     200: 정상삭제      |

![Copy of Schedule](https://github.com/user-attachments/assets/9948a4f0-284b-4918-b5ab-ed2f0504ecbc)
