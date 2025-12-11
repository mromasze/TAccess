'use client';

import { useLanguage } from '@/context/LanguageContext';
import { motion } from 'framer-motion';

const versions = [
  { version: 'v1.0.0', key: 'v1_0_0', changeCount: 3 }
];

export default function Changelog() {
  const { t } = useLanguage();

  return (
    <section id="changelog" className="py-20 px-6 relative z-10">
      <div className="container mx-auto max-w-4xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="text-4xl md:text-5xl font-bold mb-6">
            <span className="text-gradient">{t('changelog.title')}</span>
          </h2>
        </motion.div>

        <div className="space-y-8">
          {versions.map((ver, index) => (
            <motion.div
              key={ver.version}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: index * 0.1 }}
              className="glass-card p-8 rounded-2xl border border-white/5"
            >
              <div className="flex flex-col md:flex-row md:items-center justify-between mb-6 gap-4">
                <h3 className="text-2xl font-bold text-white flex items-center gap-3">
                  <span className="bg-accent-blue/20 text-accent-blue px-3 py-1 rounded-lg text-sm border border-accent-blue/30">
                    {ver.version}
                  </span>
                  <span className="text-secondary text-base font-normal">
                    {t(`changelog.${ver.key}.date`)}
                  </span>
                </h3>
              </div>
              
              <ul className="space-y-3">
                {Array.from({ length: ver.changeCount }).map((_, i) => (
                  <li key={i} className="flex items-start gap-3 text-secondary">
                    <span className="mt-1.5 w-1.5 h-1.5 rounded-full bg-accent-purple flex-shrink-0" />
                    <span>{t(`changelog.${ver.key}.changes.${i}`)}</span>
                  </li>
                ))}
              </ul>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
