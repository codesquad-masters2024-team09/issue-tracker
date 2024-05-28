<script>
    import {onMount} from "svelte";

    const words = ["Hello, World!", "Issue Tracker"];
    let i = 0;
    let j = 0;
    let currentWord = "";
    let isDeleting = false;

    function type() {
        currentWord = words[i];

        if (isDeleting) {
            document.getElementById("typewriter").textContent = currentWord.substring(0, j-1);
            j--;
            if (j == 0) {
                isDeleting = false;
                i++;
                if (i == words.length) {
                    i = 0;
                }
            }
        } else {
            document.getElementById("typewriter").textContent = currentWord.substring(0, j+1);
            j++;
            if (j == currentWord.length) {
                isDeleting = true;
            }
        }
        if (document.getElementById("typewriter").textContent === 'Issue Tracker') {
            return;
        }
        setTimeout(type, 100);
    }

    onMount(() => {
        // type();
    })
</script>

<div class="flex min-h-[80px]">
    <div class="logo-container">
        <div class="gray-text-4rem-italic">
            <a href="/" class="no-hover-color-effect animate-slidein300">
                <span id="typewriter">Issue Tracker</span>
            </a>
            <div class="logo-container-rounded z-[3] right-[120px] -bottom-[5px] animate-slidein300">
                <span class="text-[20px] mr-1.5">team Full Stack</span>
                <img loading="lazy" src="/assets/svelte_icon.svg" class="animate-bounce delay-300" alt="svelte icon" />
            </div>
            <div class="logo-container-rounded z-[2] -right-[40px] -bottom-[5px] animate-slidein300">
                <img loading="lazy" src="/assets/tailwindcss_icon.svg" class="animate-bounce delay-700" alt="tailwind icon" />
            </div>
            <div class="logo-container-rounded z-[1] -right-[60px] -bottom-[5px] animate-slidein300">
                <img loading="lazy" src="/assets/spring_icon.svg" class="animate-bounce delay-500" alt="spring icon"/>
            </div>
        </div>
    </div>
</div>