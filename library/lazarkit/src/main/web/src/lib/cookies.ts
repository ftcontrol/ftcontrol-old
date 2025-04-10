export function setCookie(name: string, value: string, maxAge = 31536000) {
  document.cookie = `${name}=${value}; path=/; max-age=${maxAge}`
}

export function getCookie(name: string): string | null {
  const match = document.cookie.match(
    new RegExp(
      `(?:^|; )${name.replace(/([.$?*|{}()[]\/+^])/g, "\\$1")}=([^;]*)`
    )
  )
  return match ? decodeURIComponent(match[1]) : null
}

export function deleteCookie(name: string) {
  document.cookie = `${name}=; path=/; max-age=0`
}
