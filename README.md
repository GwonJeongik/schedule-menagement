|    기능    | HTTP Method |                URL                 | request  | response |       상태 코드        |
|:--------:|:-----------:|:----------------------------------:|:--------:|:--------:|:------------------:|
|  일정 등록   |   `POST`    |         /../schedules/add          | 요청 body  |  등록 정보   |     200: 정상등록      |
|  일정 조회   |    `GET`    |    /../schedules/{schedule_id}     | 요청 param | 단건 응답 정보 |     200: 정상조회      |
| 일정 모두 조회 |    `GET`    |           /../schedules/           | 요청 param | 다건 응답 정보 |     200: 정상조회      |
|  일정 수정   |   `POST`    |  /../schedules/{schedule_id}/edit  | 요청 body  |  수정 정보   |     200: 정상수정      |
|  일정 삭제   |  `DELETE`   | /../schedules/{schedule_id}/delete | 요청 param |    -     |     200: 정상삭제      |

![Copy of Schedule](https://github.com/user-attachments/assets/f912a0a9-4ca0-44fb-9c37-abdf6e842a8e)
