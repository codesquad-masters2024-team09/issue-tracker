<script>
    import { auth } from '../../stores/auth.js'

    export let registerToggle

    const data = {
        memberId: '',
        password: ''
    }

    // 로그인 버튼 제어
    let isSubmitLock = true
    $: isSubmitLock = data.memberId.trim() === '' || data.password.trim() === ''

    function onRegisterMode() {
        registerToggle = true
    }

    const resetData = () => {
        data.memberId = ''
        data.password = ''
    }

    const onLogin  = async () => {
        await auth.login(data)
        resetData()
    }
</script>

<div class="login-form-input-container">
    <span class="login-form-label">아이디</span>
    <input name="memberId" bind:value={data.memberId} class="login-input-field" type="text" required
           autocomplete="username" placeholder="아이디를 입력하세요" maxlength="20"/>
</div>
<div class="login-form-input-container">
    <span class="login-form-label">비밀번호</span>
    <input name="password" bind:value={data.password} class="login-input-field" type="password" required
           autocomplete="new-password" placeholder="비밀번호를 입력하세요" maxlength="20"/>
</div>
<button type="submit" class="login-btn" disabled={isSubmitLock} on:click={onLogin}>
    <span class="mr-1 text-[16px]">
        <i class="bi bi-box-arrow-in-right"></i>
    </span>
    아이디로 로그인
</button>

<button type="button" class="go-signup-btn" on:click={onRegisterMode}>
    회원가입
</button>
