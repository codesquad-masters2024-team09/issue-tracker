/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  colors: {
    maingray: '#F7F7FC', // 메인 색상
    lightgray: '#EFF0F6', // 포커스 색상
    darkblack: '#333333', // 기본 글자 색상
    dimblack: '#555555', // 필요시
    silverblack: '#777777', // 문단 글자 색상
    lightblack: '#999999', // 필요시
    linecolor: '#f8f8f8' // 그레이 구분선 색상
  },
  theme: {
    extend: {
      fontFamily: {
        sans: ['Pretendard-Regular'],
        pretendard: ['Pretendard-Regular'],
      },
      keyframes: {
        slidein: {
          from: {
            opacity: "0",
            transform: "translateY(-10px)",
          },
          to: {
            opacity: "1",
            transform: "translateY(0)",
          },
        },
      },
      animation: {
        slidein: "slidein 0.5s",
        slidein300: "slidein 1s ease 300ms",
        slidein500: "slidein 1s ease 500ms",
        slidein700: "slidein 1s ease 700ms",
      },
    },
  },
  plugins: [],
}
