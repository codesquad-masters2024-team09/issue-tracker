import './app.css';
import App from './App.svelte'
import { auth } from "./stores/auth.js";

await auth.refresh();

const app = new App({
  target: document.getElementById('app'),
})

export default app
