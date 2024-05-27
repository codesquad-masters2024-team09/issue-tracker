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
    extend: {},
  },
  plugins: [],
}
