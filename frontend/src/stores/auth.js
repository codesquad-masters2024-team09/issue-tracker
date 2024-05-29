import { writable } from "svelte/store";
import { postApi } from "../service/api.js";
import { router } from "tinro";
import { urlPrefix } from "../utils/constants.js";

// 초기화 값 설정
const initValues = {
    memberId: '',
    profileImgUrl: '',
    accessToken: ''
};

// 인증 관련 상태 설정
function setAuth() {
    const { subscribe, set, update } = writable({ ...initValues });

    // 공통 로직을 처리하는 함수
    const handleAuthResponse = (authResponse) => {
        const payload = JSON.parse(window.atob(authResponse.accessToken.split('.')[1]));
        update(data => ({
            ...data,
            memberId: payload.memberId,
            profileImgUrl: payload.imgUrl,
            accessToken: authResponse.accessToken
        }));
        isRefresh.set(true);
    };

    // 공통 에러 핸들링 함수
    const handleError = (message) => {
        alert(message);
    };

    // 토큰 갱신 함수
    const refresh = async () => {
        try {
            const authResponse = await postApi({ path: urlPrefix + '/auth/refresh' });
            handleAuthResponse(authResponse);
        } catch (err) {
            resetUserInfo();
            isRefresh.set(false);
        }
    };

    // 사용자 정보 초기화
    const resetUserInfo = () => set({ ...initValues });

    // 로그인 함수
    const login = async (data) => {
        try {
            const options = {
                path: urlPrefix + '/auth/login',
                data: {
                    memberId: data.memberId,
                    password: data.password,
                }
            };

            const authResponse = await postApi(options);
            handleAuthResponse(authResponse);
            router.goto('/');
        } catch (error) {
            handleError('오류가 발생했습니다. 다시 로그인해 주세요.');
        }
    };

    // 로그아웃 함수
    const logout = async () => {
        try {
            await postApi({ path: urlPrefix + '/auth/logout' });
            resetUserInfo();
            isRefresh.set(false);
            router.goto("/login");
        } catch (error) {
            handleError('오류가 발생했습니다. 다시 시도해 주세요.');
        }
    };

    // 회원가입 함수
    const register = async (data) => {
        try {
            const options = {
                path: urlPrefix + '/auth/signup',
                data: {
                    memberId: data.memberId,
                    password: data.password,
                }
            };

            const authResponse = await postApi(options);
            handleAuthResponse(authResponse);
            alert('가입이 완료되었습니다.');
            router.goto('/');
        } catch (error) {
            handleError('오류가 발생했습니다. 다시 시도해 주세요.');
        }
    };

    return {
        subscribe,
        refresh,
        login,
        logout,
        resetUserInfo,
        register,
    };
}

export const auth = setAuth();
export const isRefresh = writable(false);