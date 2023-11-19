(function(){const t=document.createElement("link").relList;if(t&&t.supports&&t.supports("modulepreload"))return;for(const s of document.querySelectorAll('link[rel="modulepreload"]'))r(s);new MutationObserver(s=>{for(const o of s)if(o.type==="childList")for(const l of o.addedNodes)l.tagName==="LINK"&&l.rel==="modulepreload"&&r(l)}).observe(document,{childList:!0,subtree:!0});function n(s){const o={};return s.integrity&&(o.integrity=s.integrity),s.referrerPolicy&&(o.referrerPolicy=s.referrerPolicy),s.crossOrigin==="use-credentials"?o.credentials="include":s.crossOrigin==="anonymous"?o.credentials="omit":o.credentials="same-origin",o}function r(s){if(s.ep)return;s.ep=!0;const o=n(s);fetch(s.href,o)}})();const We=(e,t)=>e===t,ae=Symbol("solid-proxy"),He=Symbol("solid-track"),z={equals:We};let $e=_e;const k=1,Z=2,Ee={owned:null,cleanups:null,context:null,owner:null};var g=null;let le=null,w=null,S=null,T=null,re=0;function F(e,t){const n=w,r=g,s=e.length===0,o=t===void 0?r:t,l=s?Ee:{owned:null,cleanups:null,context:o?o.context:null,owner:o},i=s?e:()=>e(()=>O(()=>se(l)));g=l,w=null;try{return U(i,!0)}finally{w=n,g=r}}function L(e,t){t=t?Object.assign({},z,t):z;const n={value:e,observers:null,observerSlots:null,comparator:t.equals||void 0},r=s=>(typeof s=="function"&&(s=s(n.value)),Oe(n,s));return[Le.bind(n),r]}function I(e,t,n){const r=ye(e,t,!1,k);G(r)}function Ge(e,t,n){$e=Ze;const r=ye(e,t,!1,k);(!n||!n.render)&&(r.user=!0),T?T.push(r):G(r)}function A(e,t,n){n=n?Object.assign({},z,n):z;const r=ye(e,t,!0,0);return r.observers=null,r.observerSlots=null,r.comparator=n.equals||void 0,G(r),Le.bind(r)}function O(e){if(w===null)return e();const t=w;w=null;try{return e()}finally{w=t}}function Ce(e,t,n){const r=Array.isArray(e);let s,o=n&&n.defer;return l=>{let i;if(r){i=Array(e.length);for(let a=0;a<e.length;a++)i[a]=e[a]()}else i=e();if(o){o=!1;return}const u=O(()=>t(i,s,l));return s=i,u}}function Ve(e){Ge(()=>O(e))}function de(e){return g===null||(g.cleanups===null?g.cleanups=[e]:g.cleanups.push(e)),e}function Xe(){return g}function Je(e,t){const n=g,r=w;g=e,w=null;try{return U(t,!0)}catch(s){me(s)}finally{g=n,w=r}}function Qe(e){const t=w,n=g;return Promise.resolve().then(()=>{w=t,g=n;let r;return U(e,!1),w=g=null,r?r.done:void 0})}function Re(e,t){const n=Symbol("context");return{id:n,Provider:tt(n),defaultValue:e}}function pe(e){return g&&g.context&&g.context[e.id]!==void 0?g.context[e.id]:e.defaultValue}function ge(e){const t=A(e),n=A(()=>fe(t()));return n.toArray=()=>{const r=n();return Array.isArray(r)?r:r!=null?[r]:[]},n}function Le(){if(this.sources&&this.state)if(this.state===k)G(this);else{const e=S;S=null,U(()=>te(this),!1),S=e}if(w){const e=this.observers?this.observers.length:0;w.sources?(w.sources.push(this),w.sourceSlots.push(e)):(w.sources=[this],w.sourceSlots=[e]),this.observers?(this.observers.push(w),this.observerSlots.push(w.sources.length-1)):(this.observers=[w],this.observerSlots=[w.sources.length-1])}return this.value}function Oe(e,t,n){let r=e.value;return(!e.comparator||!e.comparator(r,t))&&(e.value=t,e.observers&&e.observers.length&&U(()=>{for(let s=0;s<e.observers.length;s+=1){const o=e.observers[s],l=le&&le.running;l&&le.disposed.has(o),(l?!o.tState:!o.state)&&(o.pure?S.push(o):T.push(o),o.observers&&Te(o)),l||(o.state=k)}if(S.length>1e6)throw S=[],new Error},!1)),t}function G(e){if(!e.fn)return;se(e);const t=g,n=w,r=re;w=g=e,Ye(e,e.value,r),w=n,g=t}function Ye(e,t,n){let r;try{r=e.fn(t)}catch(s){return e.pure&&(e.state=k,e.owned&&e.owned.forEach(se),e.owned=null),e.updatedAt=n+1,me(s)}(!e.updatedAt||e.updatedAt<=n)&&(e.updatedAt!=null&&"observers"in e?Oe(e,r):e.value=r,e.updatedAt=n)}function ye(e,t,n,r=k,s){const o={fn:e,state:r,updatedAt:null,owned:null,sources:null,sourceSlots:null,cleanups:null,value:t,owner:g,context:g?g.context:null,pure:n};return g===null||g!==Ee&&(g.owned?g.owned.push(o):g.owned=[o]),o}function ee(e){if(e.state===0)return;if(e.state===Z)return te(e);if(e.suspense&&O(e.suspense.inFallback))return e.suspense.effects.push(e);const t=[e];for(;(e=e.owner)&&(!e.updatedAt||e.updatedAt<re);)e.state&&t.push(e);for(let n=t.length-1;n>=0;n--)if(e=t[n],e.state===k)G(e);else if(e.state===Z){const r=S;S=null,U(()=>te(e,t[0]),!1),S=r}}function U(e,t){if(S)return e();let n=!1;t||(S=[]),T?n=!0:T=[],re++;try{const r=e();return ze(n),r}catch(r){n||(T=null),S=null,me(r)}}function ze(e){if(S&&(_e(S),S=null),e)return;const t=T;T=null,t.length&&U(()=>$e(t),!1)}function _e(e){for(let t=0;t<e.length;t++)ee(e[t])}function Ze(e){let t,n=0;for(t=0;t<e.length;t++){const r=e[t];r.user?e[n++]=r:ee(r)}for(t=0;t<n;t++)ee(e[t])}function te(e,t){e.state=0;for(let n=0;n<e.sources.length;n+=1){const r=e.sources[n];if(r.sources){const s=r.state;s===k?r!==t&&(!r.updatedAt||r.updatedAt<re)&&ee(r):s===Z&&te(r,t)}}}function Te(e){for(let t=0;t<e.observers.length;t+=1){const n=e.observers[t];n.state||(n.state=Z,n.pure?S.push(n):T.push(n),n.observers&&Te(n))}}function se(e){let t;if(e.sources)for(;e.sources.length;){const n=e.sources.pop(),r=e.sourceSlots.pop(),s=n.observers;if(s&&s.length){const o=s.pop(),l=n.observerSlots.pop();r<s.length&&(o.sourceSlots[l]=r,s[r]=o,n.observerSlots[r]=l)}}if(e.owned){for(t=e.owned.length-1;t>=0;t--)se(e.owned[t]);e.owned=null}if(e.cleanups){for(t=e.cleanups.length-1;t>=0;t--)e.cleanups[t]();e.cleanups=null}e.state=0}function et(e){return e instanceof Error?e:new Error(typeof e=="string"?e:"Unknown error",{cause:e})}function me(e,t=g){throw et(e)}function fe(e){if(typeof e=="function"&&!e.length)return fe(e());if(Array.isArray(e)){const t=[];for(let n=0;n<e.length;n++){const r=fe(e[n]);Array.isArray(r)?t.push.apply(t,r):t.push(r)}return t}return e}function tt(e,t){return function(r){let s;return I(()=>s=O(()=>(g.context={...g.context,[e]:r.value},ge(()=>r.children))),void 0),s}}const nt=Symbol("fallback");function Ae(e){for(let t=0;t<e.length;t++)e[t]()}function rt(e,t,n={}){let r=[],s=[],o=[],l=0,i=t.length>1?[]:null;return de(()=>Ae(o)),()=>{let u=e()||[],a,c;return u[He],O(()=>{let f=u.length,d,y,p,R,_,P,$,E,C;if(f===0)l!==0&&(Ae(o),o=[],r=[],s=[],l=0,i&&(i=[])),n.fallback&&(r=[nt],s[0]=F(V=>(o[0]=V,n.fallback())),l=1);else if(l===0){for(s=new Array(f),c=0;c<f;c++)r[c]=u[c],s[c]=F(h);l=f}else{for(p=new Array(f),R=new Array(f),i&&(_=new Array(f)),P=0,$=Math.min(l,f);P<$&&r[P]===u[P];P++);for($=l-1,E=f-1;$>=P&&E>=P&&r[$]===u[E];$--,E--)p[E]=s[$],R[E]=o[$],i&&(_[E]=i[$]);for(d=new Map,y=new Array(E+1),c=E;c>=P;c--)C=u[c],a=d.get(C),y[c]=a===void 0?-1:a,d.set(C,c);for(a=P;a<=$;a++)C=r[a],c=d.get(C),c!==void 0&&c!==-1?(p[c]=s[a],R[c]=o[a],i&&(_[c]=i[a]),c=y[c],d.set(C,c)):o[a]();for(c=P;c<f;c++)c in p?(s[c]=p[c],o[c]=R[c],i&&(i[c]=_[c],i[c](c))):s[c]=F(h);s=s.slice(0,l=f),r=u.slice(0)}return s});function h(f){if(o[c]=f,i){const[d,y]=L(c);return i[c]=y,t(u[c],d)}return t(u[c])}}}function x(e,t){return O(()=>e(t||{}))}function Q(){return!0}const st={get(e,t,n){return t===ae?n:e.get(t)},has(e,t){return t===ae?!0:e.has(t)},set:Q,deleteProperty:Q,getOwnPropertyDescriptor(e,t){return{configurable:!0,enumerable:!0,get(){return e.get(t)},set:Q,deleteProperty:Q}},ownKeys(e){return e.keys()}};function ce(e){return(e=typeof e=="function"?e():e)?e:{}}function ot(){for(let e=0,t=this.length;e<t;++e){const n=this[e]();if(n!==void 0)return n}}function it(...e){let t=!1;for(let o=0;o<e.length;o++){const l=e[o];t=t||!!l&&ae in l,e[o]=typeof l=="function"?(t=!0,A(l)):l}if(t)return new Proxy({get(o){for(let l=e.length-1;l>=0;l--){const i=ce(e[l])[o];if(i!==void 0)return i}},has(o){for(let l=e.length-1;l>=0;l--)if(o in ce(e[l]))return!0;return!1},keys(){const o=[];for(let l=0;l<e.length;l++)o.push(...Object.keys(ce(e[l])));return[...new Set(o)]}},st);const n={},r={},s=new Set;for(let o=e.length-1;o>=0;o--){const l=e[o];if(!l)continue;const i=Object.getOwnPropertyNames(l);for(let u=0,a=i.length;u<a;u++){const c=i[u];if(c==="__proto__"||c==="constructor")continue;const h=Object.getOwnPropertyDescriptor(l,c);if(!s.has(c))h.get?(s.add(c),Object.defineProperty(n,c,{enumerable:!0,configurable:!0,get:ot.bind(r[c]=[h.get.bind(l)])})):(h.value!==void 0&&s.add(c),n[c]=h.value);else{const f=r[c];f?h.get?f.push(h.get.bind(l)):h.value!==void 0&&f.push(()=>h.value):n[c]===void 0&&(n[c]=h.value)}}}return n}const lt=e=>`Stale read from <${e}>.`;function je(e){const t="fallback"in e&&{fallback:()=>e.fallback};return A(rt(()=>e.each,e.children,t||void 0))}function oe(e){const t=e.keyed,n=A(()=>e.when,void 0,{equals:(r,s)=>t?r===s:!r==!s});return A(()=>{const r=n();if(r){const s=e.children;return typeof s=="function"&&s.length>0?O(()=>s(t?r:()=>{if(!O(n))throw lt("Show");return e.when})):s}return e.fallback},void 0,void 0)}function ct(e,t,n){let r=n.length,s=t.length,o=r,l=0,i=0,u=t[s-1].nextSibling,a=null;for(;l<s||i<o;){if(t[l]===n[i]){l++,i++;continue}for(;t[s-1]===n[o-1];)s--,o--;if(s===l){const c=o<r?i?n[i-1].nextSibling:n[o-i]:u;for(;i<o;)e.insertBefore(n[i++],c)}else if(o===i)for(;l<s;)(!a||!a.has(t[l]))&&t[l].remove(),l++;else if(t[l]===n[o-1]&&n[i]===t[s-1]){const c=t[--s].nextSibling;e.insertBefore(n[i++],t[l++].nextSibling),e.insertBefore(n[--o],c),t[s]=n[o]}else{if(!a){a=new Map;let h=i;for(;h<o;)a.set(n[h],h++)}const c=a.get(t[l]);if(c!=null)if(i<c&&c<o){let h=l,f=1,d;for(;++h<s&&h<o&&!((d=a.get(t[h]))==null||d!==c+f);)f++;if(f>c-i){const y=t[l];for(;i<c;)e.insertBefore(n[i++],y)}else e.replaceChild(n[i++],t[l++])}else l++;else t[l++].remove()}}}const Se="_$DX_DELEGATE";function ut(e,t,n,r={}){let s;return F(o=>{s=o,t===document?e():B(t,e(),t.firstChild?null:void 0,n)},r.owner),()=>{s(),t.textContent=""}}function K(e,t,n){let r;const s=()=>{const l=document.createElement("template");return l.innerHTML=e,n?l.content.firstChild.firstChild:l.content.firstChild},o=t?()=>O(()=>document.importNode(r||(r=s()),!0)):()=>(r||(r=s())).cloneNode(!0);return o.cloneNode=o,o}function Ne(e,t=window.document){const n=t[Se]||(t[Se]=new Set);for(let r=0,s=e.length;r<s;r++){const o=e[r];n.has(o)||(n.add(o),t.addEventListener(o,ft))}}function at(e,t,n){n==null?e.removeAttribute(t):e.setAttribute(t,n)}function B(e,t,n,r){if(n!==void 0&&!r&&(r=[]),typeof t!="function")return ne(e,t,r,n);I(s=>ne(e,t(),s,n),r)}function ft(e){const t=`$$${e.type}`;let n=e.composedPath&&e.composedPath()[0]||e.target;for(e.target!==n&&Object.defineProperty(e,"target",{configurable:!0,value:n}),Object.defineProperty(e,"currentTarget",{configurable:!0,get(){return n||document}});n;){const r=n[t];if(r&&!n.disabled){const s=n[`${t}Data`];if(s!==void 0?r.call(n,s,e):r.call(n,e),e.cancelBubble)return}n=n._$host||n.parentNode||n.host}}function ne(e,t,n,r,s){for(;typeof n=="function";)n=n();if(t===n)return n;const o=typeof t,l=r!==void 0;if(e=l&&n[0]&&n[0].parentNode||e,o==="string"||o==="number")if(o==="number"&&(t=t.toString()),l){let i=n[0];i&&i.nodeType===3?i.data=t:i=document.createTextNode(t),n=M(e,n,r,i)}else n!==""&&typeof n=="string"?n=e.firstChild.data=t:n=e.textContent=t;else if(t==null||o==="boolean")n=M(e,n,r);else{if(o==="function")return I(()=>{let i=t();for(;typeof i=="function";)i=i();n=ne(e,i,n,r)}),()=>n;if(Array.isArray(t)){const i=[],u=n&&Array.isArray(n);if(he(i,t,n,s))return I(()=>n=ne(e,i,n,r,!0)),()=>n;if(i.length===0){if(n=M(e,n,r),l)return n}else u?n.length===0?xe(e,i,r):ct(e,n,i):(n&&M(e),xe(e,i));n=i}else if(t.nodeType){if(Array.isArray(n)){if(l)return n=M(e,n,r,t);M(e,n,null,t)}else n==null||n===""||!e.firstChild?e.appendChild(t):e.replaceChild(t,e.firstChild);n=t}}return n}function he(e,t,n,r){let s=!1;for(let o=0,l=t.length;o<l;o++){let i=t[o],u=n&&n[o],a;if(!(i==null||i===!0||i===!1))if((a=typeof i)=="object"&&i.nodeType)e.push(i);else if(Array.isArray(i))s=he(e,i,u)||s;else if(a==="function")if(r){for(;typeof i=="function";)i=i();s=he(e,Array.isArray(i)?i:[i],Array.isArray(u)?u:[u])||s}else e.push(i),s=!0;else{const c=String(i);u&&u.nodeType===3&&u.data===c?e.push(u):e.push(document.createTextNode(c))}}return s}function xe(e,t,n=null){for(let r=0,s=t.length;r<s;r++)e.insertBefore(t[r],n)}function M(e,t,n,r){if(n===void 0)return e.textContent="";const s=r||document.createTextNode("");if(t.length){let o=!1;for(let l=t.length-1;l>=0;l--){const i=t[l];if(s!==i){const u=i.parentNode===e;!o&&!l?u?e.replaceChild(s,i):e.insertBefore(s,n):u&&i.remove()}else o=!0}}else e.insertBefore(s,n);return[s]}const ht=!1;function dt(e,t,n){return e.addEventListener(t,n),()=>e.removeEventListener(t,n)}function pt([e,t],n,r){return[n?()=>n(e()):e,r?s=>t(r(s)):t]}function gt(e){try{return document.querySelector(e)}catch{return null}}function yt(e,t){const n=gt(`#${e}`);n?n.scrollIntoView():t&&window.scrollTo(0,0)}function mt(e,t,n,r){let s=!1;const o=i=>typeof i=="string"?{value:i}:i,l=pt(L(o(e()),{equals:(i,u)=>i.value===u.value}),void 0,i=>(!s&&t(i),i));return n&&de(n((i=e())=>{s=!0,l[1](o(i)),s=!1})),{signal:l,utils:r}}function wt(e){if(e){if(Array.isArray(e))return{signal:e}}else return{signal:L({value:""})};return e}function bt(){return mt(()=>({value:window.location.pathname+window.location.search+window.location.hash,state:history.state}),({value:e,replace:t,scroll:n,state:r})=>{t?window.history.replaceState(r,"",e):window.history.pushState(r,"",e),yt(window.location.hash.slice(1),n)},e=>dt(window,"popstate",()=>e()),{go:e=>window.history.go(e)})}function vt(){let e=new Set;function t(s){return e.add(s),()=>e.delete(s)}let n=!1;function r(s,o){if(n)return!(n=!1);const l={to:s,options:o,defaultPrevented:!1,preventDefault:()=>l.defaultPrevented=!0};for(const i of e)i.listener({...l,from:i.location,retry:u=>{u&&(n=!0),i.navigate(s,o)}});return!l.defaultPrevented}return{subscribe:t,confirm:r}}const At=/^(?:[a-z0-9]+:)?\/\//i,St=/^\/+|(\/)\/+$/g;function W(e,t=!1){const n=e.replace(St,"$1");return n?t||/^[?#]/.test(n)?n:"/"+n:""}function Y(e,t,n){if(At.test(t))return;const r=W(e),s=n&&W(n);let o="";return!s||t.startsWith("/")?o=r:s.toLowerCase().indexOf(r.toLowerCase())!==0?o=r+s:o=s,(o||"/")+W(t,!o)}function xt(e,t){if(e==null)throw new Error(t);return e}function ke(e,t){return W(e).replace(/\/*(\*.*)?$/g,"")+W(t)}function Pt(e){const t={};return e.searchParams.forEach((n,r)=>{t[r]=n}),t}function $t(e,t,n){const[r,s]=e.split("/*",2),o=r.split("/").filter(Boolean),l=o.length;return i=>{const u=i.split("/").filter(Boolean),a=u.length-l;if(a<0||a>0&&s===void 0&&!t)return null;const c={path:l?"":"/",params:{}},h=f=>n===void 0?void 0:n[f];for(let f=0;f<l;f++){const d=o[f],y=u[f],p=d[0]===":",R=p?d.slice(1):d;if(p&&ue(y,h(R)))c.params[R]=y;else if(p||!ue(y,d))return null;c.path+=`/${y}`}if(s){const f=a?u.slice(-a).join("/"):"";if(ue(f,h(s)))c.params[s]=f;else return null}return c}}function ue(e,t){const n=r=>r.localeCompare(e,void 0,{sensitivity:"base"})===0;return t===void 0?!0:typeof t=="string"?n(t):typeof t=="function"?t(e):Array.isArray(t)?t.some(n):t instanceof RegExp?t.test(e):!1}function Et(e){const[t,n]=e.pattern.split("/*",2),r=t.split("/").filter(Boolean);return r.reduce((s,o)=>s+(o.startsWith(":")?2:3),r.length-(n===void 0?0:1))}function Be(e){const t=new Map,n=Xe();return new Proxy({},{get(r,s){return t.has(s)||Je(n,()=>t.set(s,A(()=>e()[s]))),t.get(s)()},getOwnPropertyDescriptor(){return{enumerable:!0,configurable:!0}},ownKeys(){return Reflect.ownKeys(e())}})}function Ie(e){let t=/(\/?\:[^\/]+)\?/.exec(e);if(!t)return[e];let n=e.slice(0,t.index),r=e.slice(t.index+t[0].length);const s=[n,n+=t[1]];for(;t=/^(\/\:[^\/]+)\?/.exec(r);)s.push(n+=t[1]),r=r.slice(t[0].length);return Ie(r).reduce((o,l)=>[...o,...s.map(i=>i+l)],[])}const Ct=100,Ue=Re(),ie=Re(),we=()=>xt(pe(Ue),"Make sure your app is wrapped in a <Router />");let H;const be=()=>H||pe(ie)||we().base,Rt=()=>we().navigatorFactory(),Lt=()=>be().params;function Ot(e,t="",n){const{component:r,data:s,children:o}=e,l=!o||Array.isArray(o)&&!o.length,i={key:e,element:r?()=>x(r,{}):()=>{const{element:u}=e;return u===void 0&&n?x(n,{}):u},preload:e.component?r.preload:e.preload,data:s};return Me(e.path).reduce((u,a)=>{for(const c of Ie(a)){const h=ke(t,c),f=l?h:h.split("/*",1)[0];u.push({...i,originalPath:c,pattern:f,matcher:$t(f,!l,e.matchFilters)})}return u},[])}function _t(e,t=0){return{routes:e,score:Et(e[e.length-1])*1e4-t,matcher(n){const r=[];for(let s=e.length-1;s>=0;s--){const o=e[s],l=o.matcher(n);if(!l)return null;r.unshift({...l,route:o})}return r}}}function Me(e){return Array.isArray(e)?e:[e]}function Ke(e,t="",n,r=[],s=[]){const o=Me(e);for(let l=0,i=o.length;l<i;l++){const u=o[l];if(u&&typeof u=="object"&&u.hasOwnProperty("path")){const a=Ot(u,t,n);for(const c of a){r.push(c);const h=Array.isArray(u.children)&&u.children.length===0;if(u.children&&!h)Ke(u.children,c.pattern,n,r,s);else{const f=_t([...r],s.length);s.push(f)}r.pop()}}}return r.length?s:s.sort((l,i)=>i.score-l.score)}function Tt(e,t){for(let n=0,r=e.length;n<r;n++){const s=e[n].matcher(t);if(s)return s}return[]}function jt(e,t){const n=new URL("http://sar"),r=A(u=>{const a=e();try{return new URL(a,n)}catch{return console.error(`Invalid path ${a}`),u}},n,{equals:(u,a)=>u.href===a.href}),s=A(()=>r().pathname),o=A(()=>r().search,!0),l=A(()=>r().hash),i=A(()=>"");return{get pathname(){return s()},get search(){return o()},get hash(){return l()},get state(){return t()},get key(){return i()},query:Be(Ce(o,()=>Pt(r())))}}function Nt(e,t="",n,r){const{signal:[s,o],utils:l={}}=wt(e),i=l.parsePath||(b=>b),u=l.renderPath||(b=>b),a=l.beforeLeave||vt(),c=Y("",t),h=void 0;if(c===void 0)throw new Error(`${c} is not a valid base path`);c&&!s().value&&o({value:c,replace:!0,scroll:!1});const[f,d]=L(!1),y=async b=>{d(!0);try{await Qe(b)}finally{d(!1)}},[p,R]=L(s().value),[_,P]=L(s().state),$=jt(p,_),E=[],C={pattern:c,params:{},path:()=>c,outlet:()=>null,resolvePath(b){return Y(c,b)}};if(n)try{H=C,C.data=n({data:void 0,params:{},location:$,navigate:ve(C)})}finally{H=void 0}function V(b,m,v){O(()=>{if(typeof m=="number"){m&&(l.go?a.confirm(m,v)&&l.go(m):console.warn("Router integration does not support relative routing"));return}const{replace:X,resolve:J,scroll:j,state:q}={replace:!1,resolve:!0,scroll:!0,...v},N=J?b.resolvePath(m):Y("",m);if(N===void 0)throw new Error(`Path '${m}' is not a routable path`);if(E.length>=Ct)throw new Error("Too many redirects");const D=p();if((N!==D||q!==_())&&!ht){if(a.confirm(N,v)){const Fe=E.push({value:D,replace:X,scroll:j,state:_()});y(()=>{R(N),P(q)}).then(()=>{E.length===Fe&&De({value:N,state:q})})}}})}function ve(b){return b=b||pe(ie)||C,(m,v)=>V(b,m,v)}function De(b){const m=E[0];m&&((b.value!==m.value||b.state!==m.state)&&o({...b,replace:m.replace,scroll:m.scroll}),E.length=0)}I(()=>{const{value:b,state:m}=s();O(()=>{b!==p()&&y(()=>{R(b),P(m)})})});{let b=function(m){if(m.defaultPrevented||m.button!==0||m.metaKey||m.altKey||m.ctrlKey||m.shiftKey)return;const v=m.composedPath().find(D=>D instanceof Node&&D.nodeName.toUpperCase()==="A");if(!v||!v.hasAttribute("link"))return;const X=v.href;if(v.target||!X&&!v.hasAttribute("state"))return;const J=(v.getAttribute("rel")||"").split(/\s+/);if(v.hasAttribute("download")||J&&J.includes("external"))return;const j=new URL(X);if(j.origin!==window.location.origin||c&&j.pathname&&!j.pathname.toLowerCase().startsWith(c.toLowerCase()))return;const q=i(j.pathname+j.search+j.hash),N=v.getAttribute("state");m.preventDefault(),V(C,q,{resolve:!1,replace:v.hasAttribute("replace"),scroll:!v.hasAttribute("noscroll"),state:N&&JSON.parse(N)})};var Yt=b;Ne(["click"]),document.addEventListener("click",b),de(()=>document.removeEventListener("click",b))}return{base:C,out:h,location:$,isRouting:f,renderPath:u,parsePath:i,navigatorFactory:ve,beforeLeave:a}}function kt(e,t,n,r,s){const{base:o,location:l,navigatorFactory:i}=e,{pattern:u,element:a,preload:c,data:h}=r().route,f=A(()=>r().path);c&&c();const d={parent:t,pattern:u,get child(){return n()},path:f,params:s,data:t.data,outlet:a,resolvePath(y){return Y(o.path(),y,f())}};if(h)try{H=d,d.data=h({data:t.data,params:s,location:l,navigate:i(d)})}finally{H=void 0}return d}const Bt=e=>{const{source:t,url:n,base:r,data:s,out:o}=e,l=t||bt(),i=Nt(l,r,s);return x(Ue.Provider,{value:i,get children(){return e.children}})},It=e=>{const t=we(),n=be(),r=ge(()=>e.children),s=A(()=>Ke(r(),ke(n.pattern,e.base||""),Ut)),o=A(()=>Tt(s(),t.location.pathname)),l=Be(()=>{const c=o(),h={};for(let f=0;f<c.length;f++)Object.assign(h,c[f].params);return h});t.out&&t.out.matches.push(o().map(({route:c,path:h,params:f})=>({originalPath:c.originalPath,pattern:c.pattern,path:h,params:f})));const i=[];let u;const a=A(Ce(o,(c,h,f)=>{let d=h&&c.length===h.length;const y=[];for(let p=0,R=c.length;p<R;p++){const _=h&&h[p],P=c[p];f&&_&&P.route.key===_.route.key?y[p]=f[p]:(d=!1,i[p]&&i[p](),F($=>{i[p]=$,y[p]=kt(t,y[p-1]||n,()=>a()[p+1],()=>o()[p],l)}))}return i.splice(c.length).forEach(p=>p()),f&&d?f:(u=y[0],y)}));return x(oe,{get when(){return a()&&u},keyed:!0,children:c=>x(ie.Provider,{value:c,get children(){return c.outlet()}})})},Pe=e=>{const t=ge(()=>e.children);return it(e,{get children(){return t()}})},Ut=()=>{const e=be();return x(oe,{get when(){return e.child},keyed:!0,children:t=>x(ie.Provider,{value:t,get children(){return t.outlet()}})})};const qe="http://localhost:8080/api",Mt=async e=>{console.log(e);const t=await fetch(`${qe}/repository`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({url:e})});return console.log(t),t.json()},Kt=async e=>(await fetch(`${qe}/tags/${e}`)).json(),qt=K('<main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center"><h1 class=mb-3>GitClout</h1><form class="d-flex justify-content-center align-items-center gap-2 w-50"><input type=url class="form-control flex-fill"placeholder="URL of a git repository"><button type=submit class="btn btn-primary">Analyze</button></form><div class="mt-5 w-50"><h2 class=h3>History</h2><ul>'),Dt=K('<div id=modal class="position-absolute top-0 min-vh-100 min-vw-100 d-flex justify-content-center align-items-center"><div class="min-vh-100 min-vw-100 bg-dark bg-opacity-50"></div><div class="position-absolute bg-white p-4 rounded d-flex flex-column gap-3"><p class=h5>Resolving git repository</p><div class="d-flex justify-content-center"><div class=spinner-border role=status><span class=visually-hidden>Loading...'),Ft=K("<li><a target=_blank rel=noreferrer>");function Wt(){const e=Rt(),[t,n]=L(""),[r,s]=L(!1),o=async l=>{l.preventDefault(),l.stopPropagation(),s(!0),Mt(t()).then(i=>{s(!1);const u=i.data.repo.id;e(`/repo/${u}`)}).catch(i=>{s(!1),console.log(i)})};return[(()=>{const l=qt(),i=l.firstChild,u=i.nextSibling,a=u.firstChild,c=u.nextSibling,h=c.firstChild,f=h.nextSibling;return u.addEventListener("submit",d=>o(d)),a.$$input=d=>n(d.target.value),B(f,x(je,{each:[],children:d=>(()=>{const y=Ft(),p=y.firstChild;return B(p,()=>d.name),I(()=>at(p,"href",d.url)),y})()})),I(()=>a.value=t()),l})(),x(oe,{get when(){return r()},get children(){return Dt()}})]}Ne(["input"]);const Ht=K('<main class="container mt-5"><h1></h1><p></p><div class=mt-5><h2 class=h3>Tags</h2><ul>'),Gt=K('<div id=modal class="position-absolute top-0 min-vh-100 min-vw-100 d-flex justify-content-center align-items-center"><div class="min-vh-100 min-vw-100 bg-dark bg-opacity-50"></div><div class="position-absolute bg-white p-4 rounded d-flex flex-column gap-3"><p class=h5>Resolving tags</p><div class="d-flex justify-content-center"><div class=spinner-border role=status><span class=visually-hidden>Loading...'),Vt=K("<li>");function Xt(){const[e,t]=L(!1),[n,r]=L(""),[s,o]=L(""),[l,i]=L([]);return Ve(async()=>{const u=Lt().id;t(!0),await Kt(u).then(a=>{t(!1),r(a.data.repo.name),o(a.data.repo.URL),console.log(s()),i(a.data.tags)}).catch(a=>{t(!1),console.log(a)})}),[(()=>{const u=Ht(),a=u.firstChild,c=a.nextSibling,h=c.nextSibling,f=h.firstChild,d=f.nextSibling;return B(a,n),B(c,s),B(d,x(je,{get each(){return l()},children:y=>(()=>{const p=Vt();return B(p,()=>y.name),p})()})),u})(),x(oe,{get when(){return e()},get children(){return Gt()}})]}function Jt(){return x(It,{get children(){return[x(Pe,{path:"/",component:Wt}),x(Pe,{path:"/repo/:id",component:Xt})]}})}const Qt=document.getElementById("root");ut(()=>x(Bt,{get children(){return x(Jt,{})}}),Qt);
