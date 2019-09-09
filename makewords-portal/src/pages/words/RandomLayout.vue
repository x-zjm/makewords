<template>
  <v-form
    ref="form"
    lazy-validation
  >
    <v-layout row>
      <v-spacer />
      <!-- 选择框 -->
      <v-flex xs12 sm3 md3>
        <v-select
          :items="numbers"
          :label="selectTitle"
          v-model="selectedWordLength"
          :placeholder="defaultLength"
          ref="selectNumber"
          required
        ></v-select>
      </v-flex>

      <!-- 操作按钮 -->
      <v-btn
        :loading="loading"
        :disabled="loading"
        class="align-center"
        color="primary"
        @keyup.enter.native="submit"
        @click="submit"
        v-focus="submitFlag"
        ref="fl"
      >
        生成
      </v-btn>
      <v-btn
        class="align-center"
        color="warning"
        @click="reset"
      >
        重置
      </v-btn>
      <v-spacer />
    </v-layout>
  </v-form>
</template>

<script>

export default {
  data: function () {
    return {
      selectTitle: this.$appConfig.titles.selectNumber,  // 字数选择框标题
      loading: false,  // 按钮的加载状态
      numbers: [],  // 随机字数数组
      defaultLength: null,  // 默认字数
      selectedWordLength: null,  // 选择后的随机字数
      submitFlag: false,  // 按钮聚焦开关
    };
  },
  directives: {
    focus: function (el) {
      el.focus();
    }
  },
  created: function() {
    // 加载配置文件
    this.defaultLength = this.$appConfig.defaultLength + '';
    this.selectedWordLength = this.defaultLength;
    // 填充数组
    for (let i=this.$appConfig.minWordLength; i<=this.$appConfig.maxWordLength; i++) {
      this.numbers.push(i);
    }
  },
  props: ['wordsCards', 'showWays', 'dialogCommonContent', 'showCommonDialog', 'commonConfirmFlag'],
  mounted() {
    // 切换组词方式为true，获取焦点，判定第一次加载到这个页面
    this.submitFlag = this.showWays;
  },
  watch: {
    showWays(newval, oldval) {
      // 切换组词方式为true，获取焦点
      this.submitFlag = newval;
    },
  },
  methods: {
    /**
     * 提交表单，生成未组词的词组卡
     * @return {null}
     */
    submit () {
      // 关掉聚焦按钮
      this.submitFlag = false;
      // 超过最大限制
      if (this.selectedWordLength > this.$appConfig.maxWordLength) {
        this.dialogCommonContent = this.$appConfig.tips.wordLengthMaxLimtit;
        this.showCommonDialog = true;
        this.commonConfirmFlag = true;
        return;
      }
      // 低过最小限制
      if (this.selectedWordLength < this.$appConfig.minWordLength) {
        this.dialogCommonContent = this.$appConfig.tips.wordLengthMinLimtit;
        this.showCommonDialog = true;
        this.commonConfirmFlag = true;
        return;
      }
      // 设置按钮开始加载
      this.loading = true;
      // 发送请求
      this.$http
        .get('/word/randomword?selectedWordLength=' + this.selectedWordLength)
        .then(({ data }) => {
          // 处理时间
          data.createdTime = this.$parent.$parent.nowTime();

          // 向一套选项卡添加控制显示的两个属性：控制组词按钮显示和词语列表的组词
          // 是否组词过，默认没有组词
          data.isMaked = false;
          // 词组卡默认不显示词组
          data.showWords = false;
          
          // 结果追加到数组中
          this.wordsCards.unshift(data);
          // 设置按钮停止加载
          this.loading = null;
          this.submitFlag = true;
        })
        .catch(() => {
          this.dialogCommonContent = this.$appConfig.tips.UnknownError;
          this.showCommonDialog = true;
          this.commonConfirmFlag = true;
        });
    },
    /**
     * 重置表单
     * @return {null} 
     */
    reset () {
      this.$refs.form.reset();
      this.$refs.selectNumber.focus();
      this.selectedWordLength = this.defaultLength;
    }
  }
};
</script>

<style scoped>
.custom-loader {
  animation: loader 1s infinite;
  display: flex;
}
@-moz-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-webkit-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-o-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>