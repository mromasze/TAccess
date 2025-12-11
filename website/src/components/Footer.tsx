'use client';

import { useLanguage } from '@/context/LanguageContext';

export default function Footer() {
  const { t } = useLanguage();
  const currentYear = new Date().getFullYear();

  return (
    <footer className="py-12 px-6 border-t border-dark-border bg-dark-bg/50 backdrop-blur-sm">
      <div className="container mx-auto text-center">
        <div className="flex justify-center items-center gap-8 mb-8">
          {[
            { name: 'GitHub', url: 'https://github.com/mromasze' },
            { name: 'LinkedIn', url: 'https://linkedin.com/in/mromaszewski' },
            { name: 'Email', url: 'mailto:contact@mromaszewski.dev' }
          ].map((link) => (
            <a
              key={link.name}
              href={link.url}
              target={link.name !== 'Email' ? '_blank' : undefined}
              rel={link.name !== 'Email' ? 'noopener noreferrer' : undefined}
              className="text-secondary hover:text-accent-blue transition-colors duration-300 font-medium relative group"
            >
              {link.name}
              <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-accent-blue transition-all duration-300 group-hover:w-full" />
            </a>
          ))}
        </div>
        
        <div className="space-y-2">
          <p className="text-secondary/80">
            © {currentYear} TAccess. {t('footer.rights')}
          </p>
          <p className="text-secondary/60 text-sm">
            {t('footer.creator')}
          </p>
        </div>
      </div>
    </footer>
  );
}