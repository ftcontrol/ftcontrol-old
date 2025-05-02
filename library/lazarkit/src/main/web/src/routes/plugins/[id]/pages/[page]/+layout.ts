export function load({ params }) {
  const { page } = params

  return {
    page,
  }
}

export const prerender = false
