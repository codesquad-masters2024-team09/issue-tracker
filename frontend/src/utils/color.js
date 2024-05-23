export function hexToRgb(hex) {
    hex = hex.replace(/^#/, "");
    const bigint = parseInt(hex, 16);
    const r = (bigint >> 16) & 255;
    const g = (bigint >> 8) & 255;
    const b = bigint & 255;
    return { r, g, b };
}

export function rgbToHsl({ r, g, b }) {
    r /= 255;
    g /= 255;
    b /= 255;
    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    const l = (max + min) / 2;
    if (max === min) {
        return { h: 0, s: 0, l: Math.round(l * 100) };
    }
    const diff = max - min;
    const s = diff / (1 - Math.abs(2 * l - 1));
    let h;
    switch (max) {
        case r:
            h = ((g - b) / diff + (g < b ? 6 : 0)) % 6;
            break;
        case g:
            h = (b - r) / diff + 2;
            break;
        case b:
            h = (r - g) / diff + 4;
            break;
    }
    h = Math.round(h * 60);
    return { h, s: Math.round(s * 100), l: Math.round(l * 100) };
}