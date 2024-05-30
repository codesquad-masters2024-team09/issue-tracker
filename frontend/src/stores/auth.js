import {derived, writable} from "svelte/store";
import {delApi, postApi} from "../service/api.js";
import { router } from "tinro";
import { urlPrefix } from "../utils/constants.js";

// 인증 관련 상태 설정
function setAuth() {
    const initValues = {
        memberId: '',
        profileImgUrl: '',
        accessToken: ''
    };

    const { subscribe, set, update } = writable({ ...initValues });

    // 공통 로직을 처리하는 함수
    const handleAuthResponse = (authResponse) => {
        const payload = JSON.parse(window.atob(authResponse.accessToken.split('.')[1]));
        update(data => ({
            ...data,
            memberId: payload.sub,
            profileImgUrl: payload.imgUrl,
            accessToken: authResponse.accessToken
        }));
        isRefresh.set(true);
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
    };

    // 로그아웃 함수
    const logout = async () => {
        await postApi({ path: urlPrefix + '/auth/logout' });
        resetUserInfo();
        isRefresh.set(false);
        router.goto("/login");
    };

    // 회원가입 함수
    const register = async (data) => {
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
    };

    // 회원탈퇴 함수
    const withdraw = async () => {
        await delApi({ path: urlPrefix + '/auth/withdraw' });
        resetUserInfo();
        isRefresh.set(false);
        router.goto("/login");
    }

    return {
        subscribe,
        refresh,
        login,
        logout,
        resetUserInfo,
        register,
        withdraw
    };
}

function setIsLoggedIn() {
    return derived(auth, $auth => !!$auth.accessToken);
}

export const auth = setAuth();
export const isRefresh = writable(false);
export const isLoggedIn = setIsLoggedIn();