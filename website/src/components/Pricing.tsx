'use client';

import { useLanguage } from '@/context/LanguageContext';
import { motion } from 'framer-motion';

export default function Pricing() {
  const { t } = useLanguage();

  return (
    <section id="pricing" className="py-20 px-6 relative z-10">
      <div className="container mx-auto max-w-5xl">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="text-4xl md:text-5xl font-bold mb-6">
            <span className="text-gradient">{t('pricing.title')}</span>
          </h2>
        </motion.div>

        <div className="grid md:grid-cols-2 gap-8">
          {/* Free */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="glass-card p-8 rounded-2xl flex flex-col hover:border-accent-blue/50 transition-colors"
          >
            <h3 className="text-3xl font-bold mb-2 text-white">{t('pricing.free.title')}</h3>
            <p className="text-secondary mb-6">{t('pricing.free.description')}</p>
            <div className="text-4xl font-bold mb-6 text-white">
              $0<span className="text-lg text-secondary font-normal">/month</span>
            </div>
            <ul className="space-y-4 mb-8 flex-grow">
              {[1, 2, 3, 4].map((i) => (
                <li key={i} className="flex items-center space-x-3">
                  <div className="w-5 h-5 rounded-full bg-accent-green/20 flex items-center justify-center flex-shrink-0">
                    <svg className="w-3 h-3 text-accent-green" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                  </div>
                  <span className="text-secondary">{t(`pricing.free.feature${i}`)}</span>
                </li>
              ))}
            </ul>
            <a
              href="#installation"
              className="block w-full py-3 bg-accent-blue hover:bg-accent-blue/90 text-white rounded-xl font-semibold text-center transition-all shadow-lg hover:shadow-accent-blue/20"
            >
              {t('pricing.free.cta')}
            </a>
          </motion.div>

          {/* Commercial */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.1 }}
            className="glass-card p-8 rounded-2xl flex flex-col relative overflow-hidden border-accent-purple/30 hover:border-accent-purple/60 transition-colors"
          >
            <div className="absolute top-0 right-0 p-4">
              <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-accent-purple/20 text-accent-purple border border-accent-purple/20">
                Commercial
              </span>
            </div>
            
            <h3 className="text-3xl font-bold mb-2 text-white">{t('pricing.commercial.title')}</h3>
            <p className="text-secondary mb-6">{t('pricing.commercial.description')}</p>
            <div className="text-4xl font-bold mb-6">
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-accent-purple to-pink-500">Custom</span>
            </div>
            <ul className="space-y-4 mb-8 flex-grow">
              {[1, 2, 3, 4].map((i) => (
                <li key={i} className="flex items-center space-x-3">
                  <div className="w-5 h-5 rounded-full bg-accent-purple/20 flex items-center justify-center flex-shrink-0">
                    <svg className="w-3 h-3 text-accent-purple" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                  </div>
                  <span className="text-secondary">{t(`pricing.commercial.feature${i}`)}</span>
                </li>
              ))}
            </ul>
            <a
              href="mailto:contact@mromaszewski.dev"
              className="block w-full py-3 bg-gradient-to-r from-accent-purple to-pink-600 hover:from-accent-purple/90 hover:to-pink-600/90 text-white rounded-xl font-semibold text-center transition-all shadow-lg hover:shadow-purple-500/20"
            >
              {t('pricing.commercial.cta')}
            </a>
          </motion.div>
        </div>
      </div>
    </section>
  );
}