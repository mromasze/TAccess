/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'export',
  basePath: '/TAccess',
  assetPrefix: '/TAccess/',
  images: {
    unoptimized: true,
  },
  trailingSlash: true,
}

module.exports = nextConfig
