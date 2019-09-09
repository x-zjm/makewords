<template>
    <v-form
    ref="form"
    v-model="valid"
    @keyup.enter.native="makewords(wordsCard)"
    lazy-validation
  >
    <v-layout row>
      <v-spacer />
      <!-- 输入框 -->
      <v-flex xs12 sm3 md3>
        <v-text-field
        :label="textTitle"
        :counter="maxWordLength"
        v-model="wordText"
        :rules="textRules"
        required
        ref="inputWord"
        ></v-text-field>
      </v-flex>

      <!-- 操作按钮 -->
      <v-btn
        class="align-center"
        color="primary"
        @keyup.enter="makewords(wordsCard)"
        @click="makewords(wordsCard)"
        :disabled="!valid"
      >
        组词
      </v-btn>
      <v-btn
        class="align-center"
        color="warning"
        @keyup.enter="reset"
        @click="reset"
      >
        重置
      </v-btn>
      <v-spacer />
    </v-layout>
  </v-form>
</template>

<script>
module.exports = {
  data: function () {
    return {
      textTitle: this.$appConfig.titles.inputText,  // 输入框标题
      maxWordLength: this.$appConfig.maxWordLength,  // 最大输入字数
      wordText: '',  // 输入框内容
      valid: true, //  组词情况显示情况
      wordsCard: {}, // 点击组词后生成的一个词组卡
      textRules: [
        v => !!v || this.$appConfig.tips.notRequired,
        v => (v && v.length >= this.$appConfig.minWordLength) || this.$appConfig.tips.wordLengthMinLimtit,
        v => (v && v.length <= this.$appConfig.maxWordLength) || this.$appConfig.tips.wordLengthMaxLimtit,
        v => this.$appConfig.textRule.test(v) || this.$appConfig.tips.isNotChar
      ]
    };
  },
  props: ['showWays'],
  mounted() {
    // 切换组词方式为false，获取焦点，判定第一次加载到这个页面
    this.showWays || this.$refs.inputWord.focus();
  },
  watch: {
    showWays(newval, oldval) {
      // 切换组词方式为false，获取焦点
      console.log('input newval', newval);
      newval ? '' : this.$refs.inputWord.focus();
    }
  },
  methods: {
    /**
     * 创建为组词的词组卡并调用父组件的方法实现组词
     * @param  {[type]} wordsCard [description]
     * @return {[type]}           [description]
     */
    makewords (wordsCard) {
      // 生成词组对象
      wordsCard = { wordText: this.wordText, id: ''}
      // 调用父组件方法，成功并获取焦点
      this.$parent.$parent.makewords(wordsCard) && this.$nextTick( () =>{
        this.$refs.inputWord.focus();
      });;
    },
    /**
     * 重置表单
     * @return {null} 
     */
    reset () {
      this.$refs.form.reset();
      this.$nextTick( () =>{
        this.$refs.inputWord.focus();
      });
      this.wordNumber = this.defaultNumber;
    }
  }
};
</script>

<style scoped>
</style>