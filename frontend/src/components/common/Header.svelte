<script>
    import { auth } from "../../stores/auth.js";
    export let member;

    let isProfileClick = false;

    member = {...auth}

    const onProfileImg = () => {
        return member.profileImgUrl ? member.profileImgUrl : "../../../public/assets/profile_icon.svg";
    }

    const onProfileClick = () => {
        isProfileClick = !isProfileClick;
    }

    const onLogout = () => {
        isProfileClick = !isProfileClick;
        auth.logout()
    }

    const onWithdraw = () => {
        isProfileClick = !isProfileClick;
    }

</script>

<header class="flex mb-[5rem] justify-between items-center">
    <div id="logo">
        <a class="no-hover-color-effect text-4xl italic" href="/">Issue Tracker</a>
    </div>
    <button on:click={onProfileClick}>
        <img src={onProfileImg()} alt="User Profile Icon" class="user-profile-icon"/>
    </button>
    {#if isProfileClick}
        <div>
            <button on:click={onLogout}>로그아웃</button>
            <button on:click={onWithdraw}>회원 탈퇴</button>
        </div>
    {/if}
</header>
