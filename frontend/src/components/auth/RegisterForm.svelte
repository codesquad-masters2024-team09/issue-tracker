<script>
    export let registerToggle

    // 아이디 패스워드 감지
    let memberId = ''
    $: memberId

    // 회원가입 버튼 제어
    let password_first = ''
    $: password_first

    let password_second = ''
    $: password_second

    let isSubmitLock = true
    $: isSubmitLock = memberId.trim() === '' || password_first.trim() === '' || password_second.trim() === ''

    function onRegisterMode() {
        registerToggle = false
    }
</script>

<form action="/login" method="post">
    <div class="login-form-input-container">
        <span class="login-form-label">아이디</span>
        <input name="memberId" bind:value={memberId} class="login-input-field" type="text" required
               autocomplete="username" placeholder="아이디를 입력하세요" maxlength="20"/>
    </div>
    <div class="login-form-input-container">
        <span class="login-form-label">비밀번호</span>
        <input name="password" bind:value={password_first} class="login-input-field" type="password" required
               autocomplete="new-password" placeholder="비밀번호를 입력하세요" maxlength="20"/>
    </div>
    <div class="login-form-input-container">
        <span class="login-form-label">비밀번호 확인</span>
        <input name="password" bind:value={password_second} class="login-input-field" type="password" required
               autocomplete="new-password" placeholder="같은 비밀번호를 입력하세요" maxlength="20"/>
    </div>
    <button type="submit" class="signup-btn" disabled={isSubmitLock}>
        회원가입
    </button>

    <button type="button" class="back-btn" on:click={onRegisterMode}>
        <span class="mr-1 text-[16px]">
            <i class="bi bi-arrow-left-square"></i>
        </span>
        로그인 화면으로 돌아가기
    </button>
</form>
