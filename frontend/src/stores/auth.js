import { writable } from "svelte/store";
import { postApi } from "../service/api.js";
import { router } from "tinro";
import { urlPrefix } from "../utils/constants.js";

function setAuth() {
    let initValues = {
        memberId: '',
        profileImgUrl: '',
    }

    const { subscribe, set, update } = writable({...initValues})

    const refresh = async () => {
        try {
            const authResponse = await postApi({path: urlPrefix + '/auth/refresh'})
            
            localStorage.setItem("accessToken", authResponse.accessToken)
            localStorage.setItem("refreshToken", authResponse.refreshToken)
            isRefresh.set(true)
        }
        catch(err) {
            auth.resetUserInfo()
            isRefresh.set(false)
        }
    }

    const resetUserInfo = () => set({...initValues})

    const login = async(data) => {
        try {
            const options = {
                path: urlPrefix + '/auth/login',
                data: {
                    memberId: data.memberId,
                    password: data.password,
                }
            }

            const authResponse = await postApi(options)
            update(data => {
                const payload = JSON.parse(window.atob(authResponse.accessToken.split('.')[1]));
                data.memberId = payload.memberId;
                data.profileImgUrl = payload.imgUrl;
                return data;
            })
            
            localStorage.setItem("accessToken", authResponse.accessToken)
            localStorage.setItem("refreshToken", authResponse.refreshToken)
            
            isRefresh.set(true)
            router.goto('/')
        }
        catch(error) {
            alert('오류가 발생했습니다. 다시 로그인해 주세요.')
        }
    }

    const logout = async () => {
        try {
            const options = {
                path: urlPrefix + '/auth/logout',
            }

            await postApi(options);

            resetUserInfo();

            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");

            isRefresh.set(false)
            
            router.goto("/login")
        }
        catch(error) {
            alert('오류가 발생했습니다. 다시시도해 주세요')
        }
    }

    const register = async (data) => {
        try {
            const options = {
                path: urlPrefix + '/auth/signup',
                data: {
                    memberId: data.memberId,
                    password: data.password,
                }
            }

            const authResponse = await postApi(options)
            update(data => {
                const payload = JSON.parse(window.atob(authResponse.accessToken.split('.')[1]));
                data.memberId = payload.memberId;
                data.profileImgUrl = payload.imgUrl;
                return data;
            })

            localStorage.setItem("accessToken", authResponse.accessToken)
            localStorage.setItem("refreshToken", authResponse.refreshToken)

            isRefresh.set(true)
            alert('가입이 완료되었습니다.')
            router.goto('/')
        }
        catch(error) {
            alert('오류가 발생했습니다. 다시 시도해 주세요.')
        }
    }

    return {
        subscribe,
        refresh,
        login,
        logout,
        resetUserInfo,
        register,
    }
}

export const auth = setAuth();
export const isRefresh = writable(false)