'use client';

import { useLanguage } from '@/context/LanguageContext';
import { motion } from 'framer-motion';

export default function Header() {
  const { language, setLanguage, t } = useLanguage();

  const toggleLanguage = () => {
    setLanguage(language === 'en' ? 'pl' : 'en');
  };

  return (
    <header className="fixed top-0 left-0 right-0 z-50 bg-dark-bg/70 backdrop-blur-xl border-b border-dark-border/50 supports-[backdrop-filter]:bg-dark-bg/40">
      <nav className="container mx-auto px-6 py-4 flex items-center justify-between">
        <motion.div 
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          className="flex items-center gap-3 group cursor-pointer"
        >
          <div className="w-10 h-10 bg-gradient-to-br from-accent-blue via-accent-purple to-pink-500 rounded-xl flex items-center justify-center shadow-lg shadow-accent-blue/20 group-hover:shadow-accent-blue/40 transition-all duration-300">
            <span className="text-white font-bold text-lg">T</span>
          </div>
          <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-white to-white/70">TAccess</span>
        </motion.div>

        <motion.div 
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="hidden md:flex items-center gap-8 bg-dark-card/50 px-6 py-2 rounded-full border border-dark-border/50 backdrop-blur-md"
        >
          {['features', 'installation', 'pricing'].map((item) => (
            <a 
              key={item}
              href={`#${item}`} 
              className="text-secondary hover:text-white transition-colors text-sm font-medium capitalize"
            >
              {t(`nav.${item}`)}
            </a>
          ))}
          <div className="w-px h-4 bg-dark-border" />
          <a
            href="https://github.com/mromasze/TAccess"
            target="_blank"
            rel="noopener noreferrer"
            className="text-secondary hover:text-white transition-colors text-sm font-medium"
          >
            {t('nav.github')}
          </a>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
        >
          <button
            onClick={toggleLanguage}
            className="flex items-center gap-2 px-4 py-2 bg-dark-card/50 hover:bg-dark-card border border-dark-border hover:border-accent-blue/50 rounded-lg transition-all duration-300 group"
          >
            <span className="text-sm font-medium text-secondary group-hover:text-white">
              {language.toUpperCase()}
            </span>
          </button>
        </motion.div>
      </nav>
    </header>
  );
}