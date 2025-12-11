'use client';

import { motion } from 'framer-motion';

export default function LoadingScreen() {
  return (
    <motion.div
      initial={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.5, ease: "easeInOut" }}
      className="fixed inset-0 z-50 flex items-center justify-center bg-dark-bg"
    >
      <div className="relative flex flex-col items-center">
        {/* Glowing Background */}
        <div className="absolute inset-0 bg-accent-blue/20 blur-3xl rounded-full scale-150 animate-pulse-slow" />
        
        <div className="relative z-10 text-center">
          <motion.div
            animate={{
              scale: [1, 1.1, 1],
              opacity: [0.5, 1, 0.5],
            }}
            transition={{
              duration: 2,
              repeat: Infinity,
              ease: "easeInOut"
            }}
            className="w-24 h-24 mb-8 relative"
          >
            <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-accent-blue to-accent-purple blur-lg opacity-50" />
            <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-accent-blue to-accent-purple flex items-center justify-center">
              <span className="text-4xl font-bold text-white">T</span>
            </div>
          </motion.div>

          <motion.h2
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
            className="text-3xl font-bold mb-2"
          >
            <span className="text-gradient">TAccess</span>
          </motion.h2>
          
          <motion.div
            initial={{ width: 0 }}
            animate={{ width: "100%" }}
            transition={{ delay: 0.4, duration: 0.8 }}
            className="h-0.5 bg-gradient-to-r from-transparent via-accent-blue to-transparent w-32 mx-auto"
          />
        </div>
      </div>
    </motion.div>
  );
}