'use client';

import { useLanguage } from '@/context/LanguageContext';
import { motion } from 'framer-motion';

const features = [
  { key: 'access', icon: '🔐' },
  { key: 'legit', icon: '✅' },
  { key: 'management', icon: '👥' },
  { key: 'security', icon: '🛡️' },
];

export default function Features() {
  const { t } = useLanguage();

  return (
    <section id="features" className="py-32 px-6 relative">
      <div className="container mx-auto relative z-10">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-20"
        >
          <h2 className="text-4xl md:text-5xl font-bold mb-6">
            <span className="text-gradient">{t('features.title')}</span>
          </h2>
          <div className="h-1 w-20 bg-accent-blue mx-auto rounded-full" />
        </motion.div>

        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
          {features.map((feature, index) => (
            <motion.div
              key={feature.key}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: index * 0.1 }}
              className="glass-card p-8 rounded-2xl hover:-translate-y-2 group"
            >
              <div className="text-5xl mb-6 transform group-hover:scale-110 transition-transform duration-300">
                {feature.icon}
              </div>
              <h3 className="text-xl font-bold mb-3 text-white">
                {t(`features.${feature.key}.title`)}
              </h3>
              <p className="text-secondary leading-relaxed">
                {t(`features.${feature.key}.description`)}
              </p>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}