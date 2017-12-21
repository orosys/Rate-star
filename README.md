# Rate Star
- 별점 라이브러리.

**API**
>'RateStar'의 주요 API

```java
// Image 설정
rateStar.setStarImage(R.drawable.star);
// 별점 값 설정
rateStar.setRateStar(2.5f);
// 별점 값 가져오기
float rate = rateStar.getRateStar();
// 별점 반개 값 허용
rateStar.setEnableHalf(true);
// 별 이미지 사이 공간 (DP)
rateStar.setSpace(10);
// 별의 갯수 설정
rateStar.setRateCount(5);
// 비활성화 된 별의 투명도
rateStar.setBgAlpha(0.3f);
```
