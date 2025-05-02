export function load({ params }) {
  const { id } = params

  return {
    id,
  }
}

export const prerender = false
