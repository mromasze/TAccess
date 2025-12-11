'use client';

import { useLanguage } from '@/context/LanguageContext';
import { motion } from 'framer-motion';

export default function Installation() {
  const { t } = useLanguage();

  return (
    <section id="installation" className="py-20 px-6 relative z-10">
      <div className="container mx-auto max-w-5xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="text-4xl md:text-5xl font-bold mb-6">
            <span className="text-gradient">{t('installation.title')}</span>
          </h2>
        </motion.div>

        <div className="grid md:grid-cols-2 gap-8">
          {/* Docker */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="glass-card p-8 rounded-2xl"
          >
            <h3 className="text-2xl font-bold mb-6 text-white flex items-center gap-3">
              <span className="text-3xl">🐳</span> {t('installation.docker.title')}
            </h3>
            <div className="space-y-4">
              {[0, 1, 2, 3].map((i) => (
                <div key={i} className="flex items-start space-x-4">
                  <span className="flex-shrink-0 w-6 h-6 bg-accent-blue/20 text-accent-blue rounded-full flex items-center justify-center text-sm font-bold border border-accent-blue/30">
                    {i + 1}
                  </span>
                  <p className="text-secondary leading-relaxed">{t(`installation.docker.steps.${i}`)}</p>
                </div>
              ))}
            </div>
            <div className="mt-8 rounded-xl overflow-hidden border border-dark-border bg-dark-bg/50">
              <div className="flex items-center gap-2 px-4 py-2 bg-dark-bg border-b border-dark-border">
                <div className="w-3 h-3 rounded-full bg-red-500/50" />
                <div className="w-3 h-3 rounded-full bg-yellow-500/50" />
                <div className="w-3 h-3 rounded-full bg-green-500/50" />
              </div>
              <pre className="p-4 text-sm overflow-x-auto">
                <code className="text-accent-green font-mono">
                  {`git clone https://github.com/mromasze/TAccess.git
cd TAccess
cp .env.example .env
docker-compose up -d`}
                </code>
              </pre>
            </div>
          </motion.div>

          {/* Manual */}
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="glass-card p-8 rounded-2xl"
          >
            <h3 className="text-2xl font-bold mb-6 text-white flex items-center gap-3">
              <span className="text-3xl">🛠️</span> {t('installation.manual.title')}
            </h3>
            <div className="space-y-4">
              {[0, 1, 2, 3].map((i) => (
                <div key={i} className="flex items-start space-x-4">
                  <span className="flex-shrink-0 w-6 h-6 bg-accent-purple/20 text-accent-purple rounded-full flex items-center justify-center text-sm font-bold border border-accent-purple/30">
                    {i + 1}
                  </span>
                  <p className="text-secondary leading-relaxed">{t(`installation.manual.steps.${i}`)}</p>
                </div>
              ))}
            </div>
            <div className="mt-8 rounded-xl overflow-hidden border border-dark-border bg-dark-bg/50">
              <div className="flex items-center gap-2 px-4 py-2 bg-dark-bg border-b border-dark-border">
                <div className="w-3 h-3 rounded-full bg-red-500/50" />
                <div className="w-3 h-3 rounded-full bg-yellow-500/50" />
                <div className="w-3 h-3 rounded-full bg-green-500/50" />
              </div>
              <pre className="p-4 text-sm overflow-x-auto">
                <code className="text-accent-purple font-mono">
                  {`git clone https://github.com/mromasze/TAccess.git
cd TAccess
npm install
npm run build
npm start`}
                </code>
              </pre>
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  );
}