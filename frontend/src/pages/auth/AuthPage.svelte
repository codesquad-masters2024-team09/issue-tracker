<script>
    import Logo from "../../components/auth/Logo.svelte";
    import LoginForm from "../../components/auth/LoginForm.svelte";
    import RegisterForm from "../../components/auth/RegisterForm.svelte";
    import Footer from "../../components/common/Footer.svelte";

    let registerToggle = false
    $: registerToggle
</script>

<div class="centered-container">
    <div class="flex-col">
        <Logo />

        <div class="flex flex-col my-10 justify-center items-center">
            <button type="button" class="login-btn-github animate-slidein300">
                <span class="mr-1">
                    <i class="bi bi-github"></i>
                </span>
                GitHub 계정으로 로그인
            </button>

            <hr class="text-divider animate-slidein500">

            <div class="auth-form animate-slidein700">
            {#if !registerToggle}
                <LoginForm bind:registerToggle={registerToggle} />
            {:else}
                <RegisterForm bind:registerToggle={registerToggle} />
            {/if}
            </div>
        </div>
        <Footer />
    </div>
</div>
