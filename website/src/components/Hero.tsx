'use client';

import { useLanguage } from '@/context/LanguageContext';
import { useTypewriter } from '@/hooks/useTypewriter';
import { motion } from 'framer-motion';

export default function Hero() {
  const { t } = useLanguage();
  const subtitle = t('hero.subtitle');
  const { displayedText } = useTypewriter(subtitle, 30);

  return (
    <section className="relative min-h-screen flex items-center justify-center pt-20 px-6 overflow-hidden">
      {/* Background Glow */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[500px] h-[500px] bg-accent-blue/20 blur-[100px] rounded-full -z-10" />
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] bg-accent-purple/10 blur-[120px] rounded-full -z-10 mix-blend-screen" />

      <div className="container mx-auto text-center z-10">
        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5 }}
          className="inline-block px-4 py-1.5 mb-8 rounded-full border border-accent-blue/30 bg-accent-blue/10 backdrop-blur-md text-accent-blue text-sm font-medium"
        >
          v1.0.0 Now Available
        </motion.div>

        <motion.h1
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-6xl md:text-8xl font-bold mb-6 tracking-tight"
        >
          <span className="text-gradient drop-shadow-lg">{t('hero.title')}</span>
        </motion.h1>

        <motion.h2
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.3, duration: 0.6 }}
          className="text-2xl md:text-3xl text-secondary mb-8 min-h-[4rem] font-light"
        >
          {displayedText}
          <span className="animate-pulse text-accent-blue">|</span>
        </motion.h2>

        <motion.p
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 1.5, duration: 0.6 }}
          className="text-lg text-secondary/80 max-w-2xl mx-auto mb-12 leading-relaxed"
        >
          {t('hero.description')}
        </motion.p>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 2, duration: 0.6 }}
          className="flex flex-col sm:flex-row gap-6 justify-center"
        >
          <a
            href="#installation"
            className="group relative px-8 py-4 bg-accent-blue hover:bg-accent-blue/90 text-white rounded-xl font-semibold transition-all shadow-[0_0_20px_rgba(59,130,246,0.3)] hover:shadow-[0_0_30px_rgba(59,130,246,0.5)] overflow-hidden"
          >
            <span className="relative z-10">{t('hero.cta')}</span>
            <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent -translate-x-full group-hover:translate-x-full transition-transform duration-1000" />
          </a>
          <a
            href="https://github.com/mromasze/TAccess"
            target="_blank"
            rel="noopener noreferrer"
            className="px-8 py-4 bg-dark-card/50 border border-dark-border hover:border-accent-blue/50 text-white rounded-xl font-semibold transition-all backdrop-blur-sm hover:bg-dark-card"
          >
            {t('hero.github')}
          </a>
        </motion.div>
      </div>
    </section>
  );
}