(function(){const t=document.createElement("link").relList;if(t&&t.supports&&t.supports("modulepreload"))return;for(const s of document.querySelectorAll('link[rel="modulepreload"]'))r(s);new MutationObserver(s=>{for(const o of s)if(o.type==="childList")for(const l of o.addedNodes)l.tagName==="LINK"&&l.rel==="modulepreload"&&r(l)}).observe(document,{childList:!0,subtree:!0});function n(s){const o={};return s.integrity&&(o.integrity=s.integrity),s.referrerPolicy&&(o.referrerPolicy=s.referrerPolicy),s.crossOrigin==="use-credentials"?o.credentials="include":s.crossOrigin==="anonymous"?o.credentials="omit":o.credentials="same-origin",o}function r(s){if(s.ep)return;s.ep=!0;const o=n(s);fetch(s.href,o)}})();const Fe=(e,t)=>e===t,ie=Symbol("solid-proxy"),De=Symbol("solid-track"),J={equals:Fe};let Me=Le;const N=1,Q=2,Se={owned:null,cleanups:null,context:null,owner:null};var d=null;let re=null,m=null,S=null,k=null,Z=0;function F(e,t){const n=m,r=d,s=e.length===0,o=t===void 0?r:t,l=s?Se:{owned:null,cleanups:null,context:o?o.context:null,owner:o},i=s?e:()=>e(()=>O(()=>te(l)));d=l,m=null;try{return j(i,!0)}finally{m=n,d=r}}function I(e,t){t=t?Object.assign({},J,t):J;const n={value:e,observers:null,observerSlots:null,comparator:t.equals||void 0},r=s=>(typeof s=="function"&&(s=s(n.value)),Ce(n,s));return[Ee.bind(n),r]}function q(e,t,n){const r=Re(e,t,!1,N);ee(r)}function A(e,t,n){n=n?Object.assign({},J,n):J;const r=Re(e,t,!0,0);return r.observers=null,r.observerSlots=null,r.comparator=n.equals||void 0,ee(r),Ee.bind(r)}function O(e){if(m===null)return e();const t=m;m=null;try{return e()}finally{m=t}}function xe(e,t,n){const r=Array.isArray(e);let s,o=n&&n.defer;return l=>{let i;if(r){i=Array(e.length);for(let f=0;f<e.length;f++)i[f]=e[f]()}else i=e();if(o){o=!1;return}const c=O(()=>t(i,s,l));return s=i,c}}function ae(e){return d===null||(d.cleanups===null?d.cleanups=[e]:d.cleanups.push(e)),e}function We(){return d}function He(e,t){const n=d,r=m;d=e,m=null;try{return j(t,!0)}catch(s){de(s)}finally{d=n,m=r}}function Ge(e){const t=m,n=d;return Promise.resolve().then(()=>{m=t,d=n;let r;return j(e,!1),m=d=null,r?r.done:void 0})}function Pe(e,t){const n=Symbol("context");return{id:n,Provider:Qe(n),defaultValue:e}}function fe(e){return d&&d.context&&d.context[e.id]!==void 0?d.context[e.id]:e.defaultValue}function he(e){const t=A(e),n=A(()=>le(t()));return n.toArray=()=>{const r=n();return Array.isArray(r)?r:r!=null?[r]:[]},n}function Ee(){if(this.sources&&this.state)if(this.state===N)ee(this);else{const e=S;S=null,j(()=>Y(this),!1),S=e}if(m){const e=this.observers?this.observers.length:0;m.sources?(m.sources.push(this),m.sourceSlots.push(e)):(m.sources=[this],m.sourceSlots=[e]),this.observers?(this.observers.push(m),this.observerSlots.push(m.sources.length-1)):(this.observers=[m],this.observerSlots=[m.sources.length-1])}return this.value}function Ce(e,t,n){let r=e.value;return(!e.comparator||!e.comparator(r,t))&&(e.value=t,e.observers&&e.observers.length&&j(()=>{for(let s=0;s<e.observers.length;s+=1){const o=e.observers[s],l=re&&re.running;l&&re.disposed.has(o),(l?!o.tState:!o.state)&&(o.pure?S.push(o):k.push(o),o.observers&&Oe(o)),l||(o.state=N)}if(S.length>1e6)throw S=[],new Error},!1)),t}function ee(e){if(!e.fn)return;te(e);const t=d,n=m,r=Z;m=d=e,Ve(e,e.value,r),m=n,d=t}function Ve(e,t,n){let r;try{r=e.fn(t)}catch(s){return e.pure&&(e.state=N,e.owned&&e.owned.forEach(te),e.owned=null),e.updatedAt=n+1,de(s)}(!e.updatedAt||e.updatedAt<=n)&&(e.updatedAt!=null&&"observers"in e?Ce(e,r):e.value=r,e.updatedAt=n)}function Re(e,t,n,r=N,s){const o={fn:e,state:r,updatedAt:null,owned:null,sources:null,sourceSlots:null,cleanups:null,value:t,owner:d,context:d?d.context:null,pure:n};return d===null||d!==Se&&(d.owned?d.owned.push(o):d.owned=[o]),o}function $e(e){if(e.state===0)return;if(e.state===Q)return Y(e);if(e.suspense&&O(e.suspense.inFallback))return e.suspense.effects.push(e);const t=[e];for(;(e=e.owner)&&(!e.updatedAt||e.updatedAt<Z);)e.state&&t.push(e);for(let n=t.length-1;n>=0;n--)if(e=t[n],e.state===N)ee(e);else if(e.state===Q){const r=S;S=null,j(()=>Y(e,t[0]),!1),S=r}}function j(e,t){if(S)return e();let n=!1;t||(S=[]),k?n=!0:k=[],Z++;try{const r=e();return Xe(n),r}catch(r){n||(k=null),S=null,de(r)}}function Xe(e){if(S&&(Le(S),S=null),e)return;const t=k;k=null,t.length&&j(()=>Me(t),!1)}function Le(e){for(let t=0;t<e.length;t++)$e(e[t])}function Y(e,t){e.state=0;for(let n=0;n<e.sources.length;n+=1){const r=e.sources[n];if(r.sources){const s=r.state;s===N?r!==t&&(!r.updatedAt||r.updatedAt<Z)&&$e(r):s===Q&&Y(r,t)}}}function Oe(e){for(let t=0;t<e.observers.length;t+=1){const n=e.observers[t];n.state||(n.state=Q,n.pure?S.push(n):k.push(n),n.observers&&Oe(n))}}function te(e){let t;if(e.sources)for(;e.sources.length;){const n=e.sources.pop(),r=e.sourceSlots.pop(),s=n.observers;if(s&&s.length){const o=s.pop(),l=n.observerSlots.pop();r<s.length&&(o.sourceSlots[l]=r,s[r]=o,n.observerSlots[r]=l)}}if(e.owned){for(t=e.owned.length-1;t>=0;t--)te(e.owned[t]);e.owned=null}if(e.cleanups){for(t=e.cleanups.length-1;t>=0;t--)e.cleanups[t]();e.cleanups=null}e.state=0}function Je(e){return e instanceof Error?e:new Error(typeof e=="string"?e:"Unknown error",{cause:e})}function de(e,t=d){throw Je(e)}function le(e){if(typeof e=="function"&&!e.length)return le(e());if(Array.isArray(e)){const t=[];for(let n=0;n<e.length;n++){const r=le(e[n]);Array.isArray(r)?t.push.apply(t,r):t.push(r)}return t}return e}function Qe(e,t){return function(r){let s;return q(()=>s=O(()=>(d.context={...d.context,[e]:r.value},he(()=>r.children))),void 0),s}}const Ye=Symbol("fallback");function we(e){for(let t=0;t<e.length;t++)e[t]()}function ze(e,t,n={}){let r=[],s=[],o=[],l=0,i=t.length>1?[]:null;return ae(()=>we(o)),()=>{let c=e()||[],f,u;return c[De],O(()=>{let a=c.length,g,b,p,$,L,x,P,E,C;if(a===0)l!==0&&(we(o),o=[],r=[],s=[],l=0,i&&(i=[])),n.fallback&&(r=[Ye],s[0]=F(W=>(o[0]=W,n.fallback())),l=1);else if(l===0){for(s=new Array(a),u=0;u<a;u++)r[u]=c[u],s[u]=F(h);l=a}else{for(p=new Array(a),$=new Array(a),i&&(L=new Array(a)),x=0,P=Math.min(l,a);x<P&&r[x]===c[x];x++);for(P=l-1,E=a-1;P>=x&&E>=x&&r[P]===c[E];P--,E--)p[E]=s[P],$[E]=o[P],i&&(L[E]=i[P]);for(g=new Map,b=new Array(E+1),u=E;u>=x;u--)C=c[u],f=g.get(C),b[u]=f===void 0?-1:f,g.set(C,u);for(f=x;f<=P;f++)C=r[f],u=g.get(C),u!==void 0&&u!==-1?(p[u]=s[f],$[u]=o[f],i&&(L[u]=i[f]),u=b[u],g.set(C,u)):o[f]();for(u=x;u<a;u++)u in p?(s[u]=p[u],o[u]=$[u],i&&(i[u]=L[u],i[u](u))):s[u]=F(h);s=s.slice(0,l=a),r=c.slice(0)}return s});function h(a){if(o[u]=a,i){const[g,b]=I(u);return i[u]=b,t(c[u],g)}return t(c[u])}}}function R(e,t){return O(()=>e(t||{}))}function V(){return!0}const Ze={get(e,t,n){return t===ie?n:e.get(t)},has(e,t){return t===ie?!0:e.has(t)},set:V,deleteProperty:V,getOwnPropertyDescriptor(e,t){return{configurable:!0,enumerable:!0,get(){return e.get(t)},set:V,deleteProperty:V}},ownKeys(e){return e.keys()}};function se(e){return(e=typeof e=="function"?e():e)?e:{}}function et(){for(let e=0,t=this.length;e<t;++e){const n=this[e]();if(n!==void 0)return n}}function tt(...e){let t=!1;for(let o=0;o<e.length;o++){const l=e[o];t=t||!!l&&ie in l,e[o]=typeof l=="function"?(t=!0,A(l)):l}if(t)return new Proxy({get(o){for(let l=e.length-1;l>=0;l--){const i=se(e[l])[o];if(i!==void 0)return i}},has(o){for(let l=e.length-1;l>=0;l--)if(o in se(e[l]))return!0;return!1},keys(){const o=[];for(let l=0;l<e.length;l++)o.push(...Object.keys(se(e[l])));return[...new Set(o)]}},Ze);const n={},r={},s=new Set;for(let o=e.length-1;o>=0;o--){const l=e[o];if(!l)continue;const i=Object.getOwnPropertyNames(l);for(let c=0,f=i.length;c<f;c++){const u=i[c];if(u==="__proto__"||u==="constructor")continue;const h=Object.getOwnPropertyDescriptor(l,u);if(!s.has(u))h.get?(s.add(u),Object.defineProperty(n,u,{enumerable:!0,configurable:!0,get:et.bind(r[u]=[h.get.bind(l)])})):(h.value!==void 0&&s.add(u),n[u]=h.value);else{const a=r[u];a?h.get?a.push(h.get.bind(l)):h.value!==void 0&&a.push(()=>h.value):n[u]===void 0&&(n[u]=h.value)}}}return n}const nt=e=>`Stale read from <${e}>.`;function rt(e){const t="fallback"in e&&{fallback:()=>e.fallback};return A(ze(()=>e.each,e.children,t||void 0))}function _e(e){const t=e.keyed,n=A(()=>e.when,void 0,{equals:(r,s)=>t?r===s:!r==!s});return A(()=>{const r=n();if(r){const s=e.children;return typeof s=="function"&&s.length>0?O(()=>s(t?r:()=>{if(!O(n))throw nt("Show");return e.when})):s}return e.fallback},void 0,void 0)}function st(e,t,n){let r=n.length,s=t.length,o=r,l=0,i=0,c=t[s-1].nextSibling,f=null;for(;l<s||i<o;){if(t[l]===n[i]){l++,i++;continue}for(;t[s-1]===n[o-1];)s--,o--;if(s===l){const u=o<r?i?n[i-1].nextSibling:n[o-i]:c;for(;i<o;)e.insertBefore(n[i++],u)}else if(o===i)for(;l<s;)(!f||!f.has(t[l]))&&t[l].remove(),l++;else if(t[l]===n[o-1]&&n[i]===t[s-1]){const u=t[--s].nextSibling;e.insertBefore(n[i++],t[l++].nextSibling),e.insertBefore(n[--o],u),t[s]=n[o]}else{if(!f){f=new Map;let h=i;for(;h<o;)f.set(n[h],h++)}const u=f.get(t[l]);if(u!=null)if(i<u&&u<o){let h=l,a=1,g;for(;++h<s&&h<o&&!((g=f.get(t[h]))==null||g!==u+a);)a++;if(a>u-i){const b=t[l];for(;i<u;)e.insertBefore(n[i++],b)}else e.replaceChild(n[i++],t[l++])}else l++;else t[l++].remove()}}}const be="_$DX_DELEGATE";function ot(e,t,n,r={}){let s;return F(o=>{s=o,t===document?e():ue(t,e(),t.firstChild?null:void 0,n)},r.owner),()=>{s(),t.textContent=""}}function pe(e,t,n){let r;const s=()=>{const l=document.createElement("template");return l.innerHTML=e,n?l.content.firstChild.firstChild:l.content.firstChild},o=t?()=>O(()=>document.importNode(r||(r=s()),!0)):()=>(r||(r=s())).cloneNode(!0);return o.cloneNode=o,o}function Te(e,t=window.document){const n=t[be]||(t[be]=new Set);for(let r=0,s=e.length;r<s;r++){const o=e[r];n.has(o)||(n.add(o),t.addEventListener(o,lt))}}function it(e,t,n){n==null?e.removeAttribute(t):e.setAttribute(t,n)}function ue(e,t,n,r){if(n!==void 0&&!r&&(r=[]),typeof t!="function")return z(e,t,r,n);q(s=>z(e,t(),s,n),r)}function lt(e){const t=`$$${e.type}`;let n=e.composedPath&&e.composedPath()[0]||e.target;for(e.target!==n&&Object.defineProperty(e,"target",{configurable:!0,value:n}),Object.defineProperty(e,"currentTarget",{configurable:!0,get(){return n||document}});n;){const r=n[t];if(r&&!n.disabled){const s=n[`${t}Data`];if(s!==void 0?r.call(n,s,e):r.call(n,e),e.cancelBubble)return}n=n._$host||n.parentNode||n.host}}function z(e,t,n,r,s){for(;typeof n=="function";)n=n();if(t===n)return n;const o=typeof t,l=r!==void 0;if(e=l&&n[0]&&n[0].parentNode||e,o==="string"||o==="number")if(o==="number"&&(t=t.toString()),l){let i=n[0];i&&i.nodeType===3?i.data=t:i=document.createTextNode(t),n=B(e,n,r,i)}else n!==""&&typeof n=="string"?n=e.firstChild.data=t:n=e.textContent=t;else if(t==null||o==="boolean")n=B(e,n,r);else{if(o==="function")return q(()=>{let i=t();for(;typeof i=="function";)i=i();n=z(e,i,n,r)}),()=>n;if(Array.isArray(t)){const i=[],c=n&&Array.isArray(n);if(ce(i,t,n,s))return q(()=>n=z(e,i,n,r,!0)),()=>n;if(i.length===0){if(n=B(e,n,r),l)return n}else c?n.length===0?ve(e,i,r):st(e,n,i):(n&&B(e),ve(e,i));n=i}else if(t.nodeType){if(Array.isArray(n)){if(l)return n=B(e,n,r,t);B(e,n,null,t)}else n==null||n===""||!e.firstChild?e.appendChild(t):e.replaceChild(t,e.firstChild);n=t}}return n}function ce(e,t,n,r){let s=!1;for(let o=0,l=t.length;o<l;o++){let i=t[o],c=n&&n[o],f;if(!(i==null||i===!0||i===!1))if((f=typeof i)=="object"&&i.nodeType)e.push(i);else if(Array.isArray(i))s=ce(e,i,c)||s;else if(f==="function")if(r){for(;typeof i=="function";)i=i();s=ce(e,Array.isArray(i)?i:[i],Array.isArray(c)?c:[c])||s}else e.push(i),s=!0;else{const u=String(i);c&&c.nodeType===3&&c.data===u?e.push(c):e.push(document.createTextNode(u))}}return s}function ve(e,t,n=null){for(let r=0,s=t.length;r<s;r++)e.insertBefore(t[r],n)}function B(e,t,n,r){if(n===void 0)return e.textContent="";const s=r||document.createTextNode("");if(t.length){let o=!1;for(let l=t.length-1;l>=0;l--){const i=t[l];if(s!==i){const c=i.parentNode===e;!o&&!l?c?e.replaceChild(s,i):e.insertBefore(s,n):c&&i.remove()}else o=!0}}else e.insertBefore(s,n);return[s]}const ut=!1;function ct(e,t,n){return e.addEventListener(t,n),()=>e.removeEventListener(t,n)}function at([e,t],n,r){return[n?()=>n(e()):e,r?s=>t(r(s)):t]}function ft(e){try{return document.querySelector(e)}catch{return null}}function ht(e,t){const n=ft(`#${e}`);n?n.scrollIntoView():t&&window.scrollTo(0,0)}function dt(e,t,n,r){let s=!1;const o=i=>typeof i=="string"?{value:i}:i,l=at(I(o(e()),{equals:(i,c)=>i.value===c.value}),void 0,i=>(!s&&t(i),i));return n&&ae(n((i=e())=>{s=!0,l[1](o(i)),s=!1})),{signal:l,utils:r}}function pt(e){if(e){if(Array.isArray(e))return{signal:e}}else return{signal:I({value:""})};return e}function gt(){return dt(()=>({value:window.location.pathname+window.location.search+window.location.hash,state:history.state}),({value:e,replace:t,scroll:n,state:r})=>{t?window.history.replaceState(r,"",e):window.history.pushState(r,"",e),ht(window.location.hash.slice(1),n)},e=>ct(window,"popstate",()=>e()),{go:e=>window.history.go(e)})}function yt(){let e=new Set;function t(s){return e.add(s),()=>e.delete(s)}let n=!1;function r(s,o){if(n)return!(n=!1);const l={to:s,options:o,defaultPrevented:!1,preventDefault:()=>l.defaultPrevented=!0};for(const i of e)i.listener({...l,from:i.location,retry:c=>{c&&(n=!0),i.navigate(s,o)}});return!l.defaultPrevented}return{subscribe:t,confirm:r}}const mt=/^(?:[a-z0-9]+:)?\/\//i,wt=/^\/+|(\/)\/+$/g;function D(e,t=!1){const n=e.replace(wt,"$1");return n?t||/^[?#]/.test(n)?n:"/"+n:""}function X(e,t,n){if(mt.test(t))return;const r=D(e),s=n&&D(n);let o="";return!s||t.startsWith("/")?o=r:s.toLowerCase().indexOf(r.toLowerCase())!==0?o=r+s:o=s,(o||"/")+D(t,!o)}function bt(e,t){if(e==null)throw new Error(t);return e}function ke(e,t){return D(e).replace(/\/*(\*.*)?$/g,"")+D(t)}function vt(e){const t={};return e.searchParams.forEach((n,r)=>{t[r]=n}),t}function At(e,t,n){const[r,s]=e.split("/*",2),o=r.split("/").filter(Boolean),l=o.length;return i=>{const c=i.split("/").filter(Boolean),f=c.length-l;if(f<0||f>0&&s===void 0&&!t)return null;const u={path:l?"":"/",params:{}},h=a=>n===void 0?void 0:n[a];for(let a=0;a<l;a++){const g=o[a],b=c[a],p=g[0]===":",$=p?g.slice(1):g;if(p&&oe(b,h($)))u.params[$]=b;else if(p||!oe(b,g))return null;u.path+=`/${b}`}if(s){const a=f?c.slice(-f).join("/"):"";if(oe(a,h(s)))u.params[s]=a;else return null}return u}}function oe(e,t){const n=r=>r.localeCompare(e,void 0,{sensitivity:"base"})===0;return t===void 0?!0:typeof t=="string"?n(t):typeof t=="function"?t(e):Array.isArray(t)?t.some(n):t instanceof RegExp?t.test(e):!1}function St(e){const[t,n]=e.pattern.split("/*",2),r=t.split("/").filter(Boolean);return r.reduce((s,o)=>s+(o.startsWith(":")?2:3),r.length-(n===void 0?0:1))}function Ne(e){const t=new Map,n=We();return new Proxy({},{get(r,s){return t.has(s)||He(n,()=>t.set(s,A(()=>e()[s]))),t.get(s)()},getOwnPropertyDescriptor(){return{enumerable:!0,configurable:!0}},ownKeys(){return Reflect.ownKeys(e())}})}function je(e){let t=/(\/?\:[^\/]+)\?/.exec(e);if(!t)return[e];let n=e.slice(0,t.index),r=e.slice(t.index+t[0].length);const s=[n,n+=t[1]];for(;t=/^(\/\:[^\/]+)\?/.exec(r);)s.push(n+=t[1]),r=r.slice(t[0].length);return je(r).reduce((o,l)=>[...o,...s.map(i=>i+l)],[])}const xt=100,Be=Pe(),ne=Pe(),ge=()=>bt(fe(Be),"Make sure your app is wrapped in a <Router />");let M;const ye=()=>M||fe(ne)||ge().base,Pt=()=>ge().navigatorFactory(),Et=()=>ye().params;function Ct(e,t="",n){const{component:r,data:s,children:o}=e,l=!o||Array.isArray(o)&&!o.length,i={key:e,element:r?()=>R(r,{}):()=>{const{element:c}=e;return c===void 0&&n?R(n,{}):c},preload:e.component?r.preload:e.preload,data:s};return Ie(e.path).reduce((c,f)=>{for(const u of je(f)){const h=ke(t,u),a=l?h:h.split("/*",1)[0];c.push({...i,originalPath:u,pattern:a,matcher:At(a,!l,e.matchFilters)})}return c},[])}function Rt(e,t=0){return{routes:e,score:St(e[e.length-1])*1e4-t,matcher(n){const r=[];for(let s=e.length-1;s>=0;s--){const o=e[s],l=o.matcher(n);if(!l)return null;r.unshift({...l,route:o})}return r}}}function Ie(e){return Array.isArray(e)?e:[e]}function qe(e,t="",n,r=[],s=[]){const o=Ie(e);for(let l=0,i=o.length;l<i;l++){const c=o[l];if(c&&typeof c=="object"&&c.hasOwnProperty("path")){const f=Ct(c,t,n);for(const u of f){r.push(u);const h=Array.isArray(c.children)&&c.children.length===0;if(c.children&&!h)qe(c.children,u.pattern,n,r,s);else{const a=Rt([...r],s.length);s.push(a)}r.pop()}}}return r.length?s:s.sort((l,i)=>i.score-l.score)}function $t(e,t){for(let n=0,r=e.length;n<r;n++){const s=e[n].matcher(t);if(s)return s}return[]}function Lt(e,t){const n=new URL("http://sar"),r=A(c=>{const f=e();try{return new URL(f,n)}catch{return console.error(`Invalid path ${f}`),c}},n,{equals:(c,f)=>c.href===f.href}),s=A(()=>r().pathname),o=A(()=>r().search,!0),l=A(()=>r().hash),i=A(()=>"");return{get pathname(){return s()},get search(){return o()},get hash(){return l()},get state(){return t()},get key(){return i()},query:Ne(xe(o,()=>vt(r())))}}function Ot(e,t="",n,r){const{signal:[s,o],utils:l={}}=pt(e),i=l.parsePath||(w=>w),c=l.renderPath||(w=>w),f=l.beforeLeave||yt(),u=X("",t),h=void 0;if(u===void 0)throw new Error(`${u} is not a valid base path`);u&&!s().value&&o({value:u,replace:!0,scroll:!1});const[a,g]=I(!1),b=async w=>{g(!0);try{await Ge(w)}finally{g(!1)}},[p,$]=I(s().value),[L,x]=I(s().state),P=Lt(p,L),E=[],C={pattern:u,params:{},path:()=>u,outlet:()=>null,resolvePath(w){return X(u,w)}};if(n)try{M=C,C.data=n({data:void 0,params:{},location:P,navigate:me(C)})}finally{M=void 0}function W(w,y,v){O(()=>{if(typeof y=="number"){y&&(l.go?f.confirm(y,v)&&l.go(y):console.warn("Router integration does not support relative routing"));return}const{replace:H,resolve:G,scroll:_,state:K}={replace:!1,resolve:!0,scroll:!0,...v},T=G?w.resolvePath(y):X("",y);if(T===void 0)throw new Error(`Path '${y}' is not a routable path`);if(E.length>=xt)throw new Error("Too many redirects");const U=p();if((T!==U||K!==L())&&!ut){if(f.confirm(T,v)){const Ue=E.push({value:U,replace:H,scroll:_,state:L()});b(()=>{$(T),x(K)}).then(()=>{E.length===Ue&&Ke({value:T,state:K})})}}})}function me(w){return w=w||fe(ne)||C,(y,v)=>W(w,y,v)}function Ke(w){const y=E[0];y&&((w.value!==y.value||w.state!==y.state)&&o({...w,replace:y.replace,scroll:y.scroll}),E.length=0)}q(()=>{const{value:w,state:y}=s();O(()=>{w!==p()&&b(()=>{$(w),x(y)})})});{let w=function(y){if(y.defaultPrevented||y.button!==0||y.metaKey||y.altKey||y.ctrlKey||y.shiftKey)return;const v=y.composedPath().find(U=>U instanceof Node&&U.nodeName.toUpperCase()==="A");if(!v||!v.hasAttribute("link"))return;const H=v.href;if(v.target||!H&&!v.hasAttribute("state"))return;const G=(v.getAttribute("rel")||"").split(/\s+/);if(v.hasAttribute("download")||G&&G.includes("external"))return;const _=new URL(H);if(_.origin!==window.location.origin||u&&_.pathname&&!_.pathname.toLowerCase().startsWith(u.toLowerCase()))return;const K=i(_.pathname+_.search+_.hash),T=v.getAttribute("state");y.preventDefault(),W(C,K,{resolve:!1,replace:v.hasAttribute("replace"),scroll:!v.hasAttribute("noscroll"),state:T&&JSON.parse(T)})};var Mt=w;Te(["click"]),document.addEventListener("click",w),ae(()=>document.removeEventListener("click",w))}return{base:C,out:h,location:P,isRouting:a,renderPath:c,parsePath:i,navigatorFactory:me,beforeLeave:f}}function _t(e,t,n,r,s){const{base:o,location:l,navigatorFactory:i}=e,{pattern:c,element:f,preload:u,data:h}=r().route,a=A(()=>r().path);u&&u();const g={parent:t,pattern:c,get child(){return n()},path:a,params:s,data:t.data,outlet:f,resolvePath(b){return X(o.path(),b,a())}};if(h)try{M=g,g.data=h({data:t.data,params:s,location:l,navigate:i(g)})}finally{M=void 0}return g}const Tt=e=>{const{source:t,url:n,base:r,data:s,out:o}=e,l=t||gt(),i=Ot(l,r,s);return R(Be.Provider,{value:i,get children(){return e.children}})},kt=e=>{const t=ge(),n=ye(),r=he(()=>e.children),s=A(()=>qe(r(),ke(n.pattern,e.base||""),Nt)),o=A(()=>$t(s(),t.location.pathname)),l=Ne(()=>{const u=o(),h={};for(let a=0;a<u.length;a++)Object.assign(h,u[a].params);return h});t.out&&t.out.matches.push(o().map(({route:u,path:h,params:a})=>({originalPath:u.originalPath,pattern:u.pattern,path:h,params:a})));const i=[];let c;const f=A(xe(o,(u,h,a)=>{let g=h&&u.length===h.length;const b=[];for(let p=0,$=u.length;p<$;p++){const L=h&&h[p],x=u[p];a&&L&&x.route.key===L.route.key?b[p]=a[p]:(g=!1,i[p]&&i[p](),F(P=>{i[p]=P,b[p]=_t(t,b[p-1]||n,()=>f()[p+1],()=>o()[p],l)}))}return i.splice(u.length).forEach(p=>p()),a&&g?a:(c=b[0],b)}));return R(_e,{get when(){return f()&&c},keyed:!0,children:u=>R(ne.Provider,{value:u,get children(){return u.outlet()}})})},Ae=e=>{const t=he(()=>e.children);return tt(e,{get children(){return t()}})},Nt=()=>{const e=ye();return R(_e,{get when(){return e.child},keyed:!0,children:t=>R(ne.Provider,{value:t,get children(){return t.outlet()}})})};const jt=pe('<main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center"><h1 class=mb-3>GitClout</h1><form class="d-flex justify-content-center align-items-center gap-2 w-50"><input type=url class="form-control flex-fill"placeholder="URL of a git repository"><button type=button class="btn btn-primary">Analyze</button></form><div class="mt-5 w-50"><h2 class=h3>Hisorique</h2><ul>'),Bt=pe("<li><a target=_blank rel=noreferrer>"),It={status:"success",data:[{repo_id:12345,name:"link1",url:"https://github.com/link1"},{repo_id:67890,name:"link2",url:"https://github.com/link2"}]};function qt(){const e=Pt(),t=n=>{e(`/repo/${n}`)};return(()=>{const n=jt(),r=n.firstChild,s=r.nextSibling,o=s.firstChild,l=o.nextSibling,i=s.nextSibling,c=i.firstChild,f=c.nextSibling;return l.$$click=()=>t(1),ue(f,R(rt,{get each(){return It.data},children:u=>(()=>{const h=Bt(),a=h.firstChild;return ue(a,()=>u.name),q(()=>it(a,"href",u.url)),h})()})),n})()}Te(["click"]);const Kt=pe("<div><h1>Repo");function Ut(){const e=Et().id;return console.log(e),Kt()}function Ft(){return R(kt,{get children(){return[R(Ae,{path:"/",component:qt}),R(Ae,{path:"/repo/:id",component:Ut})]}})}const Dt=document.getElementById("root");ot(()=>R(Tt,{get children(){return R(Ft,{})}}),Dt);