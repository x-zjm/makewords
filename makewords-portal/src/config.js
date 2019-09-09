const baseUrl = 'http://api.makewords.com';
const config = {
  locale: 'zh-CN', // en-US, zh-CN
  url: baseUrl,
  debug: {
    http: false // http request log
  },
  api: `${baseUrl}/api`,
  theme: {
    primary: "#2196F3",
    secondary: "#455A64",
    accent: "#9c27b0",
    error: "#f44336",
    warning: "#FFC107",
    info: "#64B5F6",
    success: "#4caf50"
  },
  isDark: true,
  appConfig: {
    titles: {
      app: '由字组词',
      inputText: '组词的汉字组',
      selectNumber: '随机字数'
    },
    DialogContent: {
      repeat: '已存在当前汉字对应的词卡！',
      delete: '确定要删除当前词组吗？',
    },
    tips: {
      notRequired: '输入内容不可为空！',
      wordLengthMinLimtit: '不可少于2个汉字！',
      wordLengthMaxLimtit: '不可超过10个汉字！',
      isNotChar: '请输入正确汉字！',
      noContent: '当前无词语显示！',
      UnknownError: '未知错误！'
    },
    defaultLength: 4,
    maxWordLength: 10,
    minWordLength: 2,
    textRule: /^[\u4E00-\u9FA5]+$/
  }
};

export default config;
