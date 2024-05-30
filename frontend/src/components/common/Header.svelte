<script>
    import { auth } from "../../stores/auth.js";
    import ProfilePopup from "./ProfilePopup.svelte";

    export let member;

    let isProfileClick = false;

    member = {...auth}

    const onProfileImg = () => {
        return member.profileImgUrl ? member.profileImgUrl : "../../../public/assets/profile_icon.svg";
    }

    const onProfileClick = () => {
        isProfileClick = !isProfileClick;
    }

    const closePopup = () => {
        isProfileClick = false;
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
        <div class="overlay" on:click={closePopup}></div>
        <ProfilePopup bind:isProfileClick={isProfileClick} />
    {/if}
</header>