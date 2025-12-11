'use client';

import { createContext, useContext, useState, ReactNode } from 'react';

type Language = 'en' | 'pl';

interface LanguageContextType {
  language: Language;
  setLanguage: (lang: Language) => void;
  t: (key: string) => string;
}

const translations = {
  en: {
    'nav.features': 'Features',
    'nav.installation': 'Installation',
    'nav.pricing': 'Pricing',
    'nav.github': 'GitHub',
    'nav.changelog': 'Changelog',
    'hero.title': 'TAccess',
    'hero.subtitle': 'Secure Telegram Bot for Access Control & User Management',
    'hero.description': 'Self-hosted solution for managing access to your Telegram groups and channels with advanced verification features.',
    'hero.cta': 'Get Started',
    'hero.github': 'View on GitHub',
    'features.title': 'Key Features',
    'features.access.title': 'Access Control',
    'features.access.description': 'Manage who can join your groups with customizable verification rules',
    'features.legit.title': 'Legit Check',
    'features.legit.description': 'Advanced bot verification system to detect and block spam accounts',
    'features.management.title': 'User Management',
    'features.management.description': 'Complete dashboard for managing users, permissions, and access levels',
    'features.security.title': 'Security First',
    'features.security.description': 'Self-hosted solution with end-to-end encryption and data privacy',
    'changelog.title': 'Changelog',
    'changelog.v1_0_0.date': '2023-10-27',
    'changelog.v1_0_0.changes.0': 'Initial release',
    'changelog.v1_0_0.changes.1': 'Basic Telegram bot functionality',
    'changelog.v1_0_0.changes.2': 'Docker support',
    'installation.title': 'Installation',
    'installation.docker.title': '🐳 Docker (Recommended)',
    'installation.docker.steps.0': 'Clone the repository',
    'installation.docker.steps.1': 'Copy .env.example to .env and configure',
    'installation.docker.steps.2': 'Run docker-compose up -d',
    'installation.docker.steps.3': 'Access at http://localhost:3000',
    'installation.manual.title': '⚙️ Manual Installation',
    'installation.manual.steps.0': 'Install Node.js 18+ and PostgreSQL',
    'installation.manual.steps.1': 'Clone and install dependencies',
    'installation.manual.steps.2': 'Configure database and environment variables',
    'installation.manual.steps.3': 'Run npm run build && npm start',
    'pricing.title': 'Pricing',
    'pricing.free.title': 'Free',
    'pricing.free.description': 'For projects earning less than $100/month',
    'pricing.free.feature1': 'Full access to all features',
    'pricing.free.feature2': 'Self-hosted solution',
    'pricing.free.feature3': 'Community support',
    'pricing.free.feature4': 'Regular updates',
    'pricing.free.cta': 'Get Started',
    'pricing.commercial.title': 'Commercial License',
    'pricing.commercial.description': 'For projects earning over $100/month',
    'pricing.commercial.feature1': 'Everything in Free',
    'pricing.commercial.feature2': 'Priority support',
    'pricing.commercial.feature3': 'Custom features',
    'pricing.commercial.feature4': 'Commercial usage rights',
    'pricing.commercial.cta': 'Contact for License',
    'footer.rights': 'All rights reserved',
    'footer.creator': 'Created by Michał Romaszewski',
  },
  pl: {
    'nav.features': 'Funkcje',
    'nav.installation': 'Instalacja',
    'nav.pricing': 'Cennik',
    'nav.github': 'GitHub',
    'nav.changelog': 'Zmiany',
    'hero.title': 'TAccess',
    'hero.subtitle': 'Bezpieczny Bot Telegram do Kontroli Dostępu i Zarządzania Użytkownikami',
    'hero.description': 'Własne rozwiązanie do zarządzania dostępem do Twoich grup i kanałów Telegram z zaawansowanymi funkcjami weryfikacji.',
    'hero.cta': 'Rozpocznij',
    'hero.github': 'Zobacz na GitHub',
    'features.title': 'Kluczowe Funkcje',
    'features.access.title': 'Kontrola Dostępu',
    'features.access.description': 'Zarządzaj, kto może dołączyć do grup z konfigurowalnymi regułami weryfikacji',
    'features.legit.title': 'Legit Check',
    'features.legit.description': 'Zaawansowany system weryfikacji botów do wykrywania i blokowania kont spamowych',
    'features.management.title': 'Zarządzanie Użytkownikami',
    'features.management.description': 'Kompletny panel do zarządzania użytkownikami, uprawnieniami i poziomami dostępu',
    'features.security.title': 'Bezpieczeństwo Przede Wszystkim',
    'features.security.description': 'Własne rozwiązanie z szyfrowaniem end-to-end i prywatnością danych',
    'changelog.title': 'Dziennik Zmian',
    'changelog.v1_0_0.date': '2023-10-27',
    'changelog.v1_0_0.changes.0': 'Pierwsze wydanie',
    'changelog.v1_0_0.changes.1': 'Podstawowa funkcjonalność bota Telegram',
    'changelog.v1_0_0.changes.2': 'Wsparcie dla Docker',
    'installation.title': 'Instalacja',
    'installation.docker.title': '🐳 Docker (Zalecane)',
    'installation.docker.steps.0': 'Sklonuj repozytorium',
    'installation.docker.steps.1': 'Skopiuj .env.example do .env i skonfiguruj',
    'installation.docker.steps.2': 'Uruchom docker-compose up -d',
    'installation.docker.steps.3': 'Dostęp pod http://localhost:3000',
    'installation.manual.title': '⚙️ Instalacja Manualna',
    'installation.manual.steps.0': 'Zainstaluj Node.js 18+ i PostgreSQL',
    'installation.manual.steps.1': 'Sklonuj i zainstaluj zależności',
    'installation.manual.steps.2': 'Skonfiguruj bazę danych i zmienne środowiskowe',
    'installation.manual.steps.3': 'Uruchom npm run build && npm start',
    'pricing.title': 'Cennik',
    'pricing.free.title': 'Darmowa',
    'pricing.free.description': 'Dla projektów zarabiających poniżej $100/miesiąc',
    'pricing.free.feature1': 'Pełny dostęp do wszystkich funkcji',
    'pricing.free.feature2': 'Własne rozwiązanie',
    'pricing.free.feature3': 'Wsparcie społeczności',
    'pricing.free.feature4': 'Regularne aktualizacje',
    'pricing.free.cta': 'Rozpocznij',
    'pricing.commercial.title': 'Licencja Komercyjna',
    'pricing.commercial.description': 'Dla projektów zarabiających powyżej $100/miesiąc',
    'pricing.commercial.feature1': 'Wszystko z wersji Darmowej',
    'pricing.commercial.feature2': 'Priorytetowe wsparcie',
    'pricing.commercial.feature3': 'Dedykowane funkcje',
    'pricing.commercial.feature4': 'Prawa użytku komercyjnego',
    'pricing.commercial.cta': 'Skontaktuj się w sprawie Licencji',
    'footer.rights': 'Wszelkie prawa zastrzeżone',
    'footer.creator': 'Stworzony przez Michała Romaszewskiego',
  },
};

const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

export function LanguageProvider({ children }: { children: ReactNode }) {
  const [language, setLanguage] = useState<Language>('en');

  const t = (key: string): string => {
    return translations[language][key as keyof typeof translations.en] || key;
  };

  return (
    <LanguageContext.Provider value={{ language, setLanguage, t }}>
      {children}
    </LanguageContext.Provider>
  );
}

export function useLanguage() {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within LanguageProvider');
  }
  return context;
}
